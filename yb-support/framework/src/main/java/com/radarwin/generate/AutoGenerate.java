package com.radarwin.generate;

import com.radarwin.framework.util.StringUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.*;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by josh on 15/7/8.
 */
public class AutoGenerate {

    private static String projectAbsolutePath;

    private static final String defaultTemplatePath = "/template";

    private static final String defaultWebResourcePath = "/src/main/java/com/radarwin/generate/resources";

    private static final String defaultTargetRootPath = "/src/main/java";

    private static final String defaultTargetWebAppPath = "/src/main/webapp/";

    private static final String modelTemplatePath = "/model/model.btl";

    private static final String daoTemplatePath = "/dao/dao.btl";

    private static final String daoImplTemplatePath = "/dao/daoImpl.btl";

    private static final String serviceTemplatePath = "/service/service.btl";

    private static final String serviceImplTemplatePath = "/service/serviceImpl.btl";

    private static final String controllerTemplatePath = "/controller/controller.btl";

    private static final String angularAppTemplatePath = "/webapp/app/app.btl";

    private static final String angularConstantTemplatePath = "/webapp/constant/constant.btl";

    private static final String angularControllerTemplatePath = "/webapp/controller/controller.btl";

    private static final String angularViewListTemplatePath = "/webapp/view/list.btl";

    private static final String angularViewAddTemplatePath = "/webapp/view/add.btl";

    private static final String angularViewUpdateTemplatePath = "/webapp/view/update.btl";

    private static final String targetAngularAppPath = "/scripts/app";

    private static final String targetAngularConstantPath = "/scripts/constant";

    private static final String targetAngularControllerPath = "/scripts/controllers";

    private static final String targetAngularViewPath = "/views/";

    private static GroupTemplate groupTemplate = null;

    private String templatePath = null;

    private String webResourcePath = null;

    private String targetRootPath = null;

    private String targetWebAppPath = null;

    private String basePackage;

    private Connection connection;

    private boolean copyWebResource = false;

    public AutoGenerate(String jdbcIp,
                        String jdbcPort,
                        String dbName,
                        String jdbcUserName,
                        String jdbcPwd) {
        this(jdbcIp, jdbcPort, dbName, jdbcUserName, jdbcPwd, null);
    }

    public AutoGenerate(String jdbcIp,
                        String jdbcPort,
                        String dbName,
                        String jdbcUserName,
                        String jdbcPwd,
                        String templateModuleName) {

        this.targetRootPath = defaultTargetRootPath;
        this.targetWebAppPath = defaultTargetWebAppPath;

        if (StringUtil.isBlank(templateModuleName)) {
            this.templatePath = defaultTemplatePath;
        } else {
            this.templatePath = "/" + templateModuleName + defaultTemplatePath;
        }

        if (StringUtil.isBlank(templateModuleName)) {
            this.webResourcePath = defaultWebResourcePath;
        } else {
            this.webResourcePath = "/" + templateModuleName + defaultWebResourcePath;
        }

        String url = "jdbc:mysql://" + jdbcIp + ":" + jdbcPort + "/" + dbName + "?user=" +
                jdbcUserName + "&password=" + jdbcPwd + "&useUnicode=true&characterEncoding=UTF-8";

        try {
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File directory = new File("");//参数为空
            projectAbsolutePath = directory.getCanonicalPath();
            ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader(defaultTemplatePath, "utf-8");
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(classpathResourceLoader, cfg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成model
     *
     * @param tableNames
     */
    public void generateCode(String... tableNames) {

        List<String> tableList = new ArrayList<>();

        List<TableInfo> tableInfoList = new ArrayList<>();

        if (tableNames == null || tableNames.length == 0) {
            try {
                ResultSet resultSet = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"});

                while (resultSet.next()) {
                    tableList.add(resultSet.getString("TABLE_NAME"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < tableNames.length; i++) {
                try {
                    ResultSet resultSet = connection.getMetaData().getTables(null, null, tableNames[i], new String[]{"TABLE"});
                    while (resultSet.next()) {
                        tableList.add(resultSet.getString("TABLE_NAME"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (String name : tableList) {
            TableInfo tableInfo = resolveTable(name);
            tableInfoList.add(tableInfo);
        }

        for (TableInfo tableInfo : tableInfoList) {

            this.generateModel(tableInfo);

            this.generateDao(tableInfo);

            this.generateDaoImpl(tableInfo);

            this.generateService(tableInfo);

            this.generateServiceImpl(tableInfo);

            this.generateController(tableInfo);
        }

        if (copyWebResource) {
            copyWebResources();
        }

        this.generateAngularConstant(tableInfoList);

        this.generateAngularController(tableInfoList);

        this.generateAngularView(tableInfoList);
    }

    private void generateModel(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate(modelTemplatePath);
        String modelPackage = this.getBasePackage() + ".model";

        t.binding("modelPkg", modelPackage);
        t.binding("tableInfo", tableInfo);

        String str = t.render();
        generateFile(modelPackage, tableInfo.getClassName() + ".java", str);
    }

    private void generateDao(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate(daoTemplatePath);

        String daoPkg = this.getBasePackage() + ".dao";
        t.binding("modelPkg", this.getBasePackage() + ".model");
        t.binding("daoPkg", daoPkg);
        t.binding("daoImplPkg", this.getBasePackage() + ".dao.impl");
        t.binding("tableInfo", tableInfo);

        String str = t.render();
        generateFile(daoPkg, tableInfo.getClassName() + "Dao.java", str);
    }

    private void generateDaoImpl(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate(daoImplTemplatePath);

        String daoImplPkg = this.getBasePackage() + ".dao.impl";
        t.binding("modelPkg", this.getBasePackage() + ".model");
        t.binding("daoPkg", this.getBasePackage() + ".dao");
        t.binding("daoImplPkg", daoImplPkg);
        t.binding("tableInfo", tableInfo);

        String str = t.render();
        generateFile(daoImplPkg, tableInfo.getClassName() + "DaoImpl.java", str);
    }

    private void generateService(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate(serviceTemplatePath);

        String servicePkg = this.getBasePackage() + ".service";
        t.binding("modelPkg", this.getBasePackage() + ".model");
        t.binding("daoPkg", this.getBasePackage() + ".dao");
        t.binding("servicePkg", servicePkg);
        t.binding("tableInfo", tableInfo);

        String str = t.render();
        generateFile(servicePkg, tableInfo.getClassName() + "Service.java", str);
    }

    private void generateServiceImpl(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate(serviceImplTemplatePath);

        String serviceImplPkg = this.getBasePackage() + ".service.impl";
        t.binding("modelPkg", this.getBasePackage() + ".model");
        t.binding("daoPkg", this.getBasePackage() + ".dao");
        t.binding("servicePkg", this.getBasePackage() + ".service");
        t.binding("serviceImplPkg", serviceImplPkg);
        t.binding("tableInfo", tableInfo);

        String str = t.render();
        generateFile(serviceImplPkg, tableInfo.getClassName() + "ServiceImpl.java", str);
    }

    private void generateController(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate(controllerTemplatePath);

        String controllerPkg = this.getBasePackage() + ".controller";
        t.binding("modelPkg", this.getBasePackage() + ".model");
        t.binding("daoPkg", this.getBasePackage() + ".dao");
        t.binding("servicePkg", this.getBasePackage() + ".service");
        t.binding("controllerPkg", controllerPkg);
        t.binding("tableInfo", tableInfo);

        String str = t.render();
        generateFile(controllerPkg, tableInfo.getClassName() + "Controller.java", str);
    }

    private void generateAngularApp(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            Template t = groupTemplate.getTemplate(angularAppTemplatePath);
            t.binding("tableInfo", tableInfo);
            String str = t.render();
            generateResourceFile(projectAbsolutePath + targetWebAppPath + targetAngularAppPath, tableInfo.getEntityName() + ".js", str);
        }
    }

    private void generateAngularConstant(List<TableInfo> tableInfoList) {
        Template t = groupTemplate.getTemplate(angularConstantTemplatePath);
        t.binding("tableInfoList", tableInfoList);
        String str = t.render();
        generateResourceFile(projectAbsolutePath + targetWebAppPath + targetAngularConstantPath, "urlConstant.js", str);
    }

    private void generateAngularController(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            Template t = groupTemplate.getTemplate(angularControllerTemplatePath);
            t.binding("tableInfo", tableInfo);
            String str = t.render();
            generateResourceFile(projectAbsolutePath + targetWebAppPath + targetAngularControllerPath, tableInfo.getEntityName() + "Controller.js", str);
        }
    }

    private void generateAngularView(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            String modulePath = projectAbsolutePath + targetWebAppPath + targetAngularViewPath + tableInfo.getEntityName() + File.separator;
            File moduleDirectory = new File(modulePath);
            if (!moduleDirectory.exists()) {
                moduleDirectory.mkdirs();
            }

            Template t = groupTemplate.getTemplate(angularViewListTemplatePath);
            t.binding("tableInfo", tableInfo);
            String str = t.render();
            generateResourceFile(modulePath, tableInfo.getEntityName() + "List.html", str);

            t = groupTemplate.getTemplate(angularViewAddTemplatePath);
            t.binding("tableInfo", tableInfo);
            str = t.render();
            generateResourceFile(modulePath, tableInfo.getEntityName() + "Add.html", str);

            t = groupTemplate.getTemplate(angularViewUpdateTemplatePath);
            t.binding("tableInfo", tableInfo);
            str = t.render();
            generateResourceFile(modulePath, tableInfo.getEntityName() + "Update.html", str);
        }
    }

    private String convertFilePath(String pkgName) {
        String filePath = pkgName.replace(".", "/");
        return (projectAbsolutePath + targetRootPath + "/" + filePath);
    }

    private void generateFile(String pkgName, String fileName, String content) {
        String filePath = convertFilePath(pkgName);
        File file = new File(filePath);

        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(filePath + File.separator + fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content, 0, content.length());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateResourceFile(String filePath, String fileName, String content) {
        File file = new File(filePath);

        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(filePath + File.separator + fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content, 0, content.length());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TableInfo resolveTable(String tableName) {

        TableInfo tableInfo = new TableInfo();

        tableInfo.setTableName(tableName);

        int _i = tableInfo.getTableName().indexOf("_");

        String s = null;

        if (_i != -1 && _i != 0 && _i != tableInfo.getTableName().length() - 1) {
            s = convertUnderLine(tableInfo.getTableName());
        } else {
            s = tableInfo.getTableName().toLowerCase();
        }

        tableInfo.setClassName(s.substring(0, 1).toUpperCase() + s.substring(1));

        tableInfo.setEntityName(tableInfo.getClassName().substring(0, 1).toLowerCase() + tableInfo.getClassName().substring(1));

        List<FieldInfo> list = new ArrayList<>();

        try {

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, tableInfo.getTableName());
            String primaryKey = null;

            while (resultSet.next()) {
                primaryKey = resultSet.getString("COLUMN_NAME");
            }

            resultSet = databaseMetaData.getColumns(null, null, tableInfo.getTableName(), null);

            while (resultSet.next()) {
                FieldInfo fieldInfo = new FieldInfo();
                String columnName = resultSet.getString("COLUMN_NAME");

                if ("deleted".equalsIgnoreCase(columnName)
                        || "create_time".equalsIgnoreCase(columnName)
                        || "create_user".equalsIgnoreCase(columnName)
                        || "update_time".equalsIgnoreCase(columnName)
                        || "update_user".equalsIgnoreCase(columnName)) {
                    continue;
                }
                String fieldName = null;

                int _index = columnName.indexOf("_");

                if (_index != -1 && _index != 0 && _index != columnName.length() - 1) {
                    fieldName = convertUnderLine(columnName);
                } else {
                    fieldName = columnName.toLowerCase();
                }

                String methodGetName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String methodSetName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                fieldInfo.setColumnName(columnName);
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setMethodGetName(methodGetName);
                fieldInfo.setMethodSetName(methodSetName);
                fieldInfo.setFieldType(getColumnType(Integer.valueOf(resultSet.getString("DATA_TYPE"))));
                fieldInfo.setIdentityKey(false); // 取不到是不是自增长
                if (columnName.equals(primaryKey)) {
                    fieldInfo.setPrimaryKey(true);
                    tableInfo.setPrimaryKeyField(fieldInfo.getFieldName());
                }

                if (Date.class.getSimpleName().equals(fieldInfo.getFieldType())) {
                    tableInfo.setHasDate(true);
                }
                if (BigDecimal.class.getSimpleName().equals(fieldInfo.getFieldType())) {
                    tableInfo.setHasDecimal(true);
                }
                list.add(fieldInfo);
            }
            tableInfo.setFieldInfoList(list);
            return tableInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertUnderLine(String s) {
        int _index = s.indexOf("_");
        if (_index == -1) {
            return s;
        }
        String[] ss = s.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ss.length; i++) {
            if (i == 0) {
                sb.append(ss[i].toLowerCase());
            } else {
                sb.append(ss[i].substring(0, 1).toUpperCase() + ss[i].substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setTargetModuleName(String moduleName) {
        if (StringUtil.isNotBlank(moduleName)) {
            this.targetRootPath = "/" + moduleName + defaultTargetRootPath;
            this.targetWebAppPath = "/" + moduleName + defaultTargetWebAppPath;
        }
    }

    public boolean isCopyWebResource() {
        return copyWebResource;
    }

    public void setCopyWebResource(boolean copyWebResource) {
        this.copyWebResource = copyWebResource;
    }

    private String getColumnType(int type) {
        switch (type) {
            case Types.BIGINT:
                return Long.class.getSimpleName();
            case Types.BIT:
                return "boolean";
            case Types.BINARY:
                return "Byte[]";
            case Types.BLOB:
                return String.class.getSimpleName();
            case Types.BOOLEAN:
                return "boolean";
            case Types.CHAR:
                return String.class.getSimpleName();
            case Types.CLOB:
                return String.class.getSimpleName();
            case Types.DATE:
                return Date.class.getSimpleName();
            case Types.DECIMAL:
                return BigDecimal.class.getSimpleName();
            case Types.DOUBLE:
                return Double.class.getSimpleName();
            case Types.FLOAT:
                return Float.class.getSimpleName();
            case Types.INTEGER:
                return Integer.class.getSimpleName();
            case Types.LONGNVARCHAR:
                return String.class.getSimpleName();
            case Types.LONGVARBINARY:
                return "Byte[]";
            case Types.LONGVARCHAR:
                return String.class.getSimpleName();
            case Types.NCHAR:
                return String.class.getSimpleName();
            case Types.NCLOB:
                return String.class.getSimpleName();
            case Types.NUMERIC:
                return Long.class.getSimpleName();
            case Types.NVARCHAR:
                return String.class.getSimpleName();
            case Types.SMALLINT:
                return Short.class.getSimpleName();
            case Types.TIME:
                return Date.class.getSimpleName();
            case Types.TIMESTAMP:
                return Date.class.getSimpleName();
            case Types.TINYINT:
                return Byte.class.getSimpleName();
            case Types.VARBINARY:
                return "Byte[]";
            case Types.VARCHAR:
                return String.class.getSimpleName();
            default:
                return String.class.getSimpleName();
        }
    }

    private void copyWebResources() {

        String targetPath = projectAbsolutePath + this.targetWebAppPath;
        String sourcePath = projectAbsolutePath + this.webResourcePath;

        File[] files = (new File(sourcePath)).listFiles();
        System.out.println(files.length);

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //复制文件
                fileChannelCopy(files[i], new File(targetPath + files[i].getName()));
            }
            if (files[i].isDirectory()) {
                //复制目录
                String sourceDir = sourcePath + File.separator + files[i].getName();
                String targetDir = targetPath + File.separator + files[i].getName();

                copyDirectory(sourceDir, targetDir);
            }
        }
    }

    /**
     * 复制文件
     *
     * @param s
     * @param t
     */
    private void fileChannelCopy(File s, File t) {

        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);

            fo = new FileOutputStream(t);

            in = fi.getChannel();//得到对应的文件通道

            out = fo.getChannel();//得到对应的文件通道

            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void copyDirectory(String sourceDir, String targetDir) {

        try {
            //新建目标目录
            File targetDirect = new File(targetDir);
            if (!targetDirect.exists()) {
                targetDirect.mkdirs();
            }

            //获取源文件夹当下的文件或目录
            File[] file = (new File(sourceDir)).listFiles();

            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile()) {
                    //源文件
                    File sourceFile = file[i];

                    //目标文件
                    File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());

                    if (!targetFile.exists()) {
                        fileChannelCopy(sourceFile, targetFile);
                    }
                }

                if (file[i].isDirectory()) {
                    //准备复制的源文件夹
                    String dir1 = sourceDir + File.separator + file[i].getName();

                    //准备复制的目标文件夹
                    String dir2 = targetDir + File.separator + file[i].getName();

                    copyDirectory(dir1, dir2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


