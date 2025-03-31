@call cd ..

@rem Dowloading dependencies
@if not exist utils (
	@echo Cloning git repo for project utils
    @call git clone --branch 2.0-SNAPSHOT --single-branch https://github.com/Pierre-Emmanuel41/utils
	
	@call cd utils
) else ( 
	@call cd utils
	
	@echo Pulling latest changes for project utils
	@call git pull
)

@rem Building dependencies
@echo Building project utils
@call mvn clean package install

@echo Building project protocol
@call cd ../protocol
@call mvn clean package install