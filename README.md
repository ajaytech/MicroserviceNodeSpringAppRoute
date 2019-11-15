# Microservices With Spring Boot and approuter using XSUAA
## Link to Full Course: 

### https://www.ui5cn.com/courses/sap-cloud-platform-professional-development

![UI5CN Course](https://thinkific-import.s3.amazonaws.com/17035/Ieao7C5YQOq5YiDdQyht_scp-professional-devimage.jpg)

## Steps for Running App: 
* Step 1: Git Clone:
```
git clone <URL of Repo>
```
* Step 2: Go inside the folder where pom.xml file is present(in Spring Boot App)
* Step 3: Execute Command to Create war file for Java Spring Boot
```
 mvn clean install
 ```
 * Step 4(Optional): Go inside folder approuter 
```
 npm install
 ```
* Step 5: Create XSUAA Service
```
 cf create-service xsuaa application xsuaa -c xs-security.json
 ```
 * Step 6: Deploy App to SAP® Cloud Foundry. 
  Go inside folder where manifest.yml file is present and run:
```
cf push
```

Pre-requisite:
1. Maven should be installed and Path must be set.
2. NPM should be present(Optional)
3. Account(Trial or Paid in SAP® Cloud Foundry)
 
 
