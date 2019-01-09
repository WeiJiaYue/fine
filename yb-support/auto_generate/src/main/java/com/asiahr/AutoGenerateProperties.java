package com.asiahr;

/**
 * Created by louis on 17-3-16.
 */
public class AutoGenerateProperties {


    private boolean neededModel = false;
    private boolean neededMapper = false;
    private boolean neededController = false;
    private boolean neededService = false;
    private boolean neededView = false;
    private boolean neededJs = false;
    private boolean neededAll = false;
    private String projectName = "";
    private String basePackage = "";
    private boolean generatedInResourcesPath=false;

    private AutoGenerateProperties(
            boolean neededModel,
            boolean neededMapper,
            boolean neededController,
            boolean neededService,
            boolean neededView,
            boolean neededJs,
            boolean neededAll,
            String projectName,
            String basePackage,
            boolean generatedInResourcesPath
    ) {
        this.neededModel = neededModel;
        this.neededMapper = neededMapper;
        this.neededController = neededController;
        this.neededService = neededService;
        this.neededView = neededView;
        this.neededJs = neededJs;
        this.neededAll = neededAll;
        this.projectName=projectName;
        this.basePackage=basePackage;
        this.generatedInResourcesPath=generatedInResourcesPath;
    }

    public boolean isNeededModel() {
        return neededModel;
    }

    public boolean isNeededMapper() {
        return neededMapper;
    }

    public boolean isNeededController() {
        return neededController;
    }

    public boolean isNeededService() {
        return neededService;
    }

    public boolean isNeededView() {
        return neededView;
    }

    public boolean isNeededJs() {
        return neededJs;
    }

    public boolean isNeededAll() {
        return neededAll;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getBasePackage() {
        return basePackage;
    }


    public boolean isGeneratedInResourcesPath() {
        return generatedInResourcesPath;
    }

    public static final class Builder {
        private boolean neededModel = false;
        private boolean neededMapper = false;
        private boolean neededController = false;
        private boolean neededService = false;
        private boolean neededView = false;
        private boolean neededJs = false;
        private boolean neededAll = false;
        private String projectName = "";
        private String basePackage = "";
        private boolean generatedInResourcesPath;

        public Builder() {
        }


        public Builder neededModel(boolean neededModel) {
            this.neededModel = neededModel;
            return this;
        }

        public Builder neededMapper(boolean neededMapper) {
            this.neededMapper = neededMapper;
            return this;
        }

        public Builder neededController(boolean neededController) {
            this.neededController = neededController;
            return this;
        }

        public Builder neededService(boolean neededService) {
            this.neededService = neededService;
            return this;
        }

        public Builder neededView(boolean neededView) {
            this.neededView = neededView;
            return this;
        }

        public Builder neededJs(boolean neededJs) {
            this.neededJs = neededJs;
            return this;
        }

        public Builder neededAll(boolean neededAll) {
            this.neededAll = neededAll;
            return this;
        }

        public Builder inProject(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder inBasePackage(String basePackage){
            this.basePackage=basePackage;
            return this;
        }

        public Builder generatedInResourcesPath(boolean flag){
            this.generatedInResourcesPath=flag;
            return this;
        }

        public AutoGenerateProperties build() {
            return new AutoGenerateProperties
                    (neededModel,
                            neededMapper,
                            neededController,
                            neededService,
                            neededView,
                            neededJs,
                            neededAll,
                            projectName,
                            basePackage,
                            generatedInResourcesPath
                    );
        }
    }
}

