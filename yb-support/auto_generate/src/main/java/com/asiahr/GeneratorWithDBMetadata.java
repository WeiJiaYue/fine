package com.asiahr;

import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.*;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


public class GeneratorWithDBMetadata implements AutoGenerator {

    private static final Logger logger = Logger.getLogger(GeneratorWithDBMetadata.class.getSimpleName());
    private static final String DEFAULT_TEMPLATE_PATH = "/asiahrTemplate";
    private static final String charEncoding = Charset.defaultCharset().name();
    private static String rootDir = System.getProperty("user.dir");
    private static final String entityDir = "model";
    private static final String controllerDir = "controller";
    private static final String serviceDir = "service";
    private static final String serviceImplDir = serviceDir + ".impl";
    private String mapperDir = "mapper";
    private static final String jsDir = "assets.views";
    private static final String viewDir = "views";

    private AutoGenerateProperties autoGenerateProperties;
    private static GroupTemplate groupTemplate = null;
    private String templatePath = DEFAULT_TEMPLATE_PATH;
    private String webResourcePath;
    private String targetRootPath;
    private String targetWebAppPath;
    private Connection connection;
    private boolean copyWebResource;
    private DatabaseDialect databaseDialect;

    public GeneratorWithDBMetadata(ConnectionCreator connectionCreator) {
        this.databaseDialect = connectionCreator;
        try {
            this.connection = connectionCreator.getConnection();
            this.init();
        } catch (Exception e) {
            logger.warning("create AutoGenerator error :" + e);
        }
    }

    public GeneratorWithDBMetadata(ConnectionCreator connectionCreator, AutoGenerateProperties prop) {
        try {
            this.connection = connectionCreator.getConnection();
            this.autoGenerateProperties = prop;
            this.init();
        } catch (Exception e) {
            logger.warning("create AutoGenerator error :" + e);
        }
    }


    private void init() {
        this.webResourcePath = null;
        this.copyWebResource = false;
        this.targetRootPath = "/src/main/java";
        this.targetWebAppPath = "/src/main/webapp";
        try {
            ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader(templatePath, charEncoding);
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(classpathResourceLoader, cfg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GeneratorWithDBMetadata changeDefaultTemplate(String template) {
        this.templatePath = template;
        init();
        return this;
    }


    @Override
    public void generateCode(String... tableNames) {
        try {
            if(this.connection==null){
                throw new RuntimeException("connection is null");
            }
            ArrayList<String> tableList = new ArrayList<>();
            ArrayList<TableInfo> tableInfoList = new ArrayList<>();
            if (tableNames != null && tableNames.length != 0) {
                for (int i = 0; i < tableNames.length; ++i) {
                    ResultSet tableInfo = this.connection.getMetaData().getTables(null, null, tableNames[i], new String[]{"TABLE"});
                    while (tableInfo.next()) {
                        tableList.add(tableInfo.getString("TABLE_NAME"));
                    }
                }
            } else {
                ResultSet tables = this.connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
                while (tables.next()) {
                    tableList.add(tables.getString("TABLE_NAME"));
                }
            }
            Iterator iterator = tableList.iterator();
            while (iterator.hasNext()) {
                String tableName = (String) iterator.next();
                TableInfo tableInfo = this.resolveTableV2(tableName);
                this.resolveComment(tableInfo);
                tableInfoList.add(tableInfo);
            }
            iterator = tableInfoList.iterator();
            while (iterator.hasNext()) {
                TableInfo tableInfo = (TableInfo) iterator.next();
                AutoGenerateProperties prop = this.autoGenerateProperties;
                if (prop.isNeededController()) {
                    generateController(tableInfo);
                }
                if (prop.isNeededMapper()) {
                    generateMapper(tableInfo);
                    generateMapperXml(tableInfo);
                }
                if (prop.isNeededService()) {
                    generateService(tableInfo);
                    generateServiceImpl(tableInfo);
                }
                if (prop.isNeededJs()) {
                    generateJs(tableInfo);
                }
                if (prop.isNeededView()) {
                    generateListView(tableInfo);
                }
                if (prop.isNeededModel()) {
                    this.generateModel(tableInfo);
                }
                if (prop.isNeededAll()) {
                    generateController(tableInfo);
                    generateService(tableInfo);
                    generateServiceImpl(tableInfo);
                    generateMapper(tableInfo);
                    generateMapperXml(tableInfo);
                    generateJs(tableInfo);
                    generateListView(tableInfo);
                    generateModel(tableInfo);
                }
            }
            if (this.copyWebResource) {
                this.copyWebResources();
            }
        } catch (Exception e) {
            logger.warning("generate error ,cause:" + e);
            throw new RuntimeException(e);
        }
        logger.info("code has been generated successfully");
    }


    private void generateController(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/controller/controller.btl");
        String controllerPackage = this.autoGenerateProperties.getBasePackage() + "." + controllerDir;
        t.binding("controllerPkg", controllerPackage);
        t.binding("tableInfo", tableInfo);
        t.binding("servicePkg", this.autoGenerateProperties.getBasePackage() + "."+serviceDir);
        t.binding("modelPkg", this.autoGenerateProperties.getBasePackage() + "." + entityDir);
        t.binding("projectPkg", this.autoGenerateProperties.getBasePackage());
        String str = t.render();
        this.generateFile(controllerPackage, tableInfo.getClassName() + "Controller.java", str);
    }

    private void generateService(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/service/service.btl");
        String servicePackage = this.autoGenerateProperties.getBasePackage() + "." + serviceDir;
        t.binding("servicePkg", servicePackage);
        t.binding("tableInfo", tableInfo);
        t.binding("modelPkg", this.autoGenerateProperties.getBasePackage() + "." + entityDir);
        String str = t.render();
        this.generateFile(servicePackage, tableInfo.getClassName() + "Service.java", str);
    }

    private void generateServiceImpl(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/service/serviceImpl.btl");
        String serviceImplPackage = this.autoGenerateProperties.getBasePackage() + "." + serviceImplDir;
        t.binding("serviceImplPkg", serviceImplPackage);
        t.binding("tableInfo", tableInfo);
        t.binding("modelPkg", this.autoGenerateProperties.getBasePackage() + "." + entityDir);
        t.binding("mapperPkg", this.autoGenerateProperties.getBasePackage() + "." + mapperDir);
        t.binding("servicePkg", this.autoGenerateProperties.getBasePackage() + "." + serviceDir);
        t.binding("projectPkg", this.autoGenerateProperties.getBasePackage());
        String str = t.render();
        this.generateFile(serviceImplPackage, tableInfo.getClassName() + "ServiceImpl.java", str);
    }

    private void generateMapper(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/mapper/mapper.btl");
        String mapperPackage = this.autoGenerateProperties.getBasePackage() + "." + mapperDir;
        t.binding("mapperPkg", mapperPackage);
        t.binding("tableInfo", tableInfo);
        t.binding("modelPkg", this.autoGenerateProperties.getBasePackage() + "." + entityDir);
        t.binding("projectPkg", this.autoGenerateProperties.getBasePackage());
        String str = t.render();
        this.generateFile(mapperPackage, tableInfo.getClassName() + "Mapper.java", str);
    }

    private void generateMapperXml(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/mapper/mapperXml.btl");
        String mapperXmlPackage = this.autoGenerateProperties.getBasePackage() + "." + mapperDir;
        if (this.autoGenerateProperties.isGeneratedInResourcesPath()) {
            mapperXmlPackage = ".resources." + mapperDir;
        }
        t.binding("mapperXmlPkg", mapperXmlPackage);
        t.binding("tableInfo", tableInfo);
        t.binding("modelPkg", this.autoGenerateProperties.getBasePackage() + "." + entityDir);
        t.binding("mapperPkg", this.autoGenerateProperties.getBasePackage() + "." + mapperDir);
//        this.setBasePackage("");
        String str = t.render();

        this.generateXmlFile(mapperXmlPackage, tableInfo.getClassName() + "Mapper.xml", str);

    }

    private void generateModel(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/model/model.btl");
        String modelPackage = this.autoGenerateProperties.getBasePackage() + "." + entityDir;
        t.binding("modelPkg", modelPackage);
        t.binding("tableInfo", tableInfo);
        String str = t.render();
        this.generateFile(modelPackage, tableInfo.getClassName() + ".java", str);
    }

    private void generateJs(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/js/js.btl");
        String jsPackage = jsDir + "." + tableInfo.getEntityName();
        t.binding("modelPkg", jsPackage);
        t.binding("tableInfo", tableInfo);
        String str = t.render();
        this.generateResourceFile(jsPackage, tableInfo.getEntityName() + ".js", str);

    }

    private void generateListView(TableInfo tableInfo) {
        Template t = groupTemplate.getTemplate("/view/list.btl");
        String listPackage = viewDir + "." + tableInfo.getEntityName();
        t.binding("tableInfo", tableInfo);
        String str = t.render();
        this.generateResourceFile(listPackage, tableInfo.getEntityName() + ".html", str);
    }

    private String convertFilePath(String pkgName) {
        String filePath = pkgName.replace(".", "/");
        return rootDir + "/" + this.autoGenerateProperties.getProjectName() + this.targetRootPath + "/" + filePath;
    }

    private String convertWebAppPath(String pkgName) {
        String filePath = pkgName.replace(".", "/");
        return rootDir + "/" + this.autoGenerateProperties.getProjectName() + this.targetWebAppPath + "/" + filePath;
    }

    private String convertXmlPath(String pkgName) {
        String filePath = pkgName.replace(".", "/");
        return rootDir + "/" + this.autoGenerateProperties.getProjectName() + "/src/main/" + filePath;
    }

    private void generateResourceFile(String packageName, String fileName, String content) {
        String filePath = convertWebAppPath(packageName);
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(filePath + File.separator + fileName);

        try {
            if (file.exists()) {
                return;
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        FileWriter e = null;
        try {
            e = new FileWriter(file);
            e.write(content, 0, content.length());
            e.flush();

        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            if (e != null) {
                try {
                    e.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    private void generateFile(String pkgName, String fileName, String content) {
        String filePath = this.convertFilePath(pkgName);
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(filePath + File.separator + fileName);
        try {
            if (file.exists()) {
                return;
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            FileWriter e = new FileWriter(file);
            e.write(content, 0, content.length());
            e.flush();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void generateXmlFile(String pkgName, String fileName, String content) {
        String filePath;

        if (this.autoGenerateProperties.isGeneratedInResourcesPath()) {
            filePath = this.convertXmlPath(pkgName);
        } else {
            filePath = this.convertFilePath(pkgName);
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(filePath + File.separator + fileName);
        try {
            if (file.exists()) {
                return;
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            FileWriter e = new FileWriter(file);
            e.write(content, 0, content.length());
            e.flush();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String convertUnderLine(String table) {
        int index = table.indexOf("_");
        if (index == -1) {
            return table;
        } else {
            String tableName = table;
            if (table.startsWith("T_") || table.startsWith("t_")) {
                tableName = table.substring("t_".length());
            }
            String[] ss = tableName.split("_");
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < ss.length; ++i) {
                if (i == 0) {
                    sb.append(ss[i].toLowerCase());
                } else {
                    sb.append(ss[i].substring(0, 1).toUpperCase()).append(ss[i].substring(1).toLowerCase());
                }
            }

            return sb.toString();
        }
    }


    public void setTargetModuleName(String moduleName) {
        if (StringUtils.isNotBlank(moduleName)) {
            this.targetRootPath = "/" + moduleName + "/src/main/java";
            this.targetWebAppPath = "/" + moduleName + "/src/main/webapp/";
        }

    }

    public boolean isCopyWebResource() {
        return this.copyWebResource;
    }

    public void setCopyWebResource(boolean copyWebResource) {
        this.copyWebResource = copyWebResource;
    }

    private String getColumnType(int type) {
        switch (type) {
            case -16:
                return String.class.getSimpleName();
            case -15:
                return String.class.getSimpleName();
            case -9:
                return String.class.getSimpleName();
            case -7:
                return "boolean";
            case -6:
                return Byte.class.getSimpleName();
            case -5:
                return Long.class.getSimpleName();
            case -4:
                return "Byte[]";
            case -3:
                return "Byte[]";
            case -2:
                return "Byte[]";
            case -1:
                return String.class.getSimpleName();
            case 1:
                return String.class.getSimpleName();
            case 2:
                return Long.class.getSimpleName();
            case 3:
                return BigDecimal.class.getSimpleName();
            case 4:
                return Integer.class.getSimpleName();
            case 5:
                return Short.class.getSimpleName();
            case 6:
                return Float.class.getSimpleName();
            case 8:
                return Double.class.getSimpleName();
            case 12:
                return String.class.getSimpleName();
            case 16:
                return "boolean";
            case 91:
                return Date.class.getSimpleName();
            case 92:
                return Date.class.getSimpleName();
            case 93:
                return Date.class.getSimpleName();
            case 2004:
                return String.class.getSimpleName();
            case 2005:
                return String.class.getSimpleName();
            case 2011:
                return String.class.getSimpleName();
            default:
                return String.class.getSimpleName();
        }
    }

    private void copyWebResources() {
        String targetPath = rootDir + this.targetWebAppPath;
        String sourcePath = rootDir + this.webResourcePath;
        File[] files = (new File(sourcePath)).listFiles();
        System.out.println(files.length);

        for (int i = 0; i < files.length; ++i) {
            if (files[i].isFile()) {
                this.fileChannelCopy(files[i], new File(targetPath + files[i].getName()));
            }

            if (files[i].isDirectory()) {
                String sourceDir = sourcePath + File.separator + files[i].getName();
                String targetDir = targetPath + File.separator + files[i].getName();
                this.copyDirectory(sourceDir, targetDir);
            }
        }

    }

    private void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0L, in.size(), out);
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
            File e = new File(targetDir);
            if (!e.exists()) {
                e.mkdirs();
            }

            File[] file = (new File(sourceDir)).listFiles();

            for (int i = 0; i < file.length; ++i) {
                if (file[i].isFile()) {
                    File dir1 = file[i];
                    File dir2 = new File((new File(targetDir)).getAbsolutePath() + File.separator + file[i].getName());
                    if (!dir2.exists()) {
                        this.fileChannelCopy(dir1, dir2);
                    }
                }

                if (file[i].isDirectory()) {
                    String var9 = sourceDir + File.separator + file[i].getName();
                    String var10 = targetDir + File.separator + file[i].getName();
                    this.copyDirectory(var9, var10);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void resolveComment(TableInfo tableInfo) {

        String sql;
        boolean isMysql = false;
        String dialect = databaseDialect.getDialect();
        if (dialect.equalsIgnoreCase("mysql")) {
            sql = "show full columns from " + tableInfo.getTableName();
            isMysql = true;

        } else {
            sql = "SELECT * FROM USER_COL_COMMENTS WHERE TABLE_NAME='" + tableInfo.getTableName() + "'";
        }
        List<FieldInfo> fields = tableInfo.getFieldInfoList();
        Iterator iterator = fields.iterator();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next() && iterator.hasNext()) {
                FieldInfo fieldInfo = (FieldInfo) iterator.next();
                if (isMysql) {
                    if (resultSet.getString("Field").equals(fieldInfo.getColumnName())) {
                        fieldInfo.setComment(resultSet.getString("Comment"));
                    }
                } else {
                    if (resultSet.getString("COLUMN_NAME").equals(fieldInfo.getColumnName())) {
                        fieldInfo.setComment(resultSet.getString("COMMENTS"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.warning("sql exception :" + e.getMessage() + ", sql state:" + e.getSQLState() + ", error code:" + e.getErrorCode());
        }


    }


    /**
     * new version method to resolve table by metadata that is faster than
     * @see com.asiahr.GeneratorWithDBMetadata#resolveTable(String) in efficiency
     *
     * @param tableName
     * @return
     */
    public TableInfo resolveTableV2(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        int index = tableInfo.getTableName().indexOf("_");
        String entityName = null;
        if (index != -1 && index != 0 && index != tableInfo.getTableName().length() - 1) {
            entityName = this.convertUnderLine(tableInfo.getTableName());
        } else {
            entityName = tableInfo.getTableName().toLowerCase();
        }
        tableInfo.setClassName(entityName.substring(0, 1).toUpperCase() + entityName.substring(1));
        tableInfo.setEntityName(entityName);
        ArrayList<FieldInfo> list = new ArrayList<>();

        try {
            DatabaseMetaData e = this.connection.getMetaData();
            ResultSet resultSet = e.getPrimaryKeys(null, null, tableInfo.getTableName());

            String primaryKey;
            for (primaryKey = null; resultSet.next(); primaryKey = resultSet.getString("COLUMN_NAME")) {

            }
            resultSet = e.getColumns(null, null, tableInfo.getTableName(), null);
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String columnInLowerCase = columnName.toLowerCase();
                switch (columnInLowerCase) {
                    case "deleted":
                    case "create_time":
                    case "create_user":
                    case "update_user":
                    case "update_time":
                        continue;
                }
                FieldInfo fieldInfo = new FieldInfo();
                String fieldName = null;
                int indexOf = columnName.indexOf("_");
                if (indexOf != -1 && indexOf != 0 && indexOf != columnName.length() - 1) {
                    fieldName = this.convertUnderLine(columnName);
                } else {
                    fieldName = columnName.toLowerCase();
                }

                String methodGetName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String methodSetName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                fieldInfo.setColumnName(columnName);
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setMethodGetName(methodGetName);
                fieldInfo.setMethodSetName(methodSetName);
                fieldInfo.setFieldType(this.getColumnType(Integer.valueOf(resultSet.getString("DATA_TYPE"))));
                fieldInfo.setIdentityKey(false);
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return tableInfo;
    }


    @Deprecated
    /**
     * original version for resolve table by metadata**/
    public TableInfo resolveTable(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        int index = tableInfo.getTableName().indexOf("_");
        String entityName = null;
        if (index != -1 && index != 0 && index != tableInfo.getTableName().length() - 1) {
            entityName = this.convertUnderLine(tableInfo.getTableName());
        } else {
            entityName = tableInfo.getTableName().toLowerCase();
        }
        tableInfo.setClassName(entityName.substring(0, 1).toUpperCase() + entityName.substring(1));
        tableInfo.setEntityName(entityName);
        ArrayList<FieldInfo> list = new ArrayList<>();

        try {
            DatabaseMetaData e = this.connection.getMetaData();
            ResultSet resultSet = e.getPrimaryKeys(null, null, tableInfo.getTableName());

            String primaryKey;
            for (primaryKey = null; resultSet.next(); primaryKey = resultSet.getString("COLUMN_NAME")) {

            }
            resultSet = e.getColumns(null, null, tableInfo.getTableName(), null);
            while (true) {
                FieldInfo fieldInfo;
                String columnName;
                do {
                    do {
                        do {
                            do {
                                do {
                                    if (!resultSet.next()) {
                                        tableInfo.setFieldInfoList(list);
                                        return tableInfo;
                                    }

                                    fieldInfo = new FieldInfo();
                                    columnName = resultSet.getString("COLUMN_NAME");
                                } while ("deleted".equalsIgnoreCase(columnName));
                            } while ("create_time".equalsIgnoreCase(columnName));
                        } while ("create_per".equalsIgnoreCase(columnName));
                    } while ("update_time".equalsIgnoreCase(columnName));
                } while ("update_per".equalsIgnoreCase(columnName));

                String fieldName = null;
                int indexOf = columnName.indexOf("_");
                if (indexOf != -1 && indexOf != 0 && indexOf != columnName.length() - 1) {
                    fieldName = this.convertUnderLine(columnName);
                } else {
                    fieldName = columnName.toLowerCase();
                }

                String methodGetName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String methodSetName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                fieldInfo.setColumnName(columnName);
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setMethodGetName(methodGetName);
                fieldInfo.setMethodSetName(methodSetName);
                fieldInfo.setFieldType(this.getColumnType(Integer.valueOf(resultSet.getString("DATA_TYPE"))));
                fieldInfo.setIdentityKey(false);
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public AutoGenerator propertiesSet(AutoGenerateProperties autoGenerateProperties) {
        this.autoGenerateProperties = autoGenerateProperties;
        return this;
    }


    public GeneratorWithDBMetadata changeMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
        return this;
    }

    public GeneratorWithDBMetadata setRootDir(String rootDir) {
        GeneratorWithDBMetadata.rootDir = rootDir;
        return this;
    }
}
