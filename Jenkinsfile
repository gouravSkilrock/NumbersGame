node {
   stage 'Checkout'

      checkout scm

   stage 'Build'
         sh "export" 
         def mvnHome = tool 'M3'
         def buildStatus=-1
         def  commitId
       if("${env.BRANCH_NAME}".contains("Release")) {
 slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Started For: ${BRANCH_NAME}",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'

       // commented the find bugs and pmd buildStatus = sh returnStatus: true, script: "${mvnHome}/bin/mvn clean package -DVERSION=${env.BUILD_NUMBER} findbugs:findbugs pmd:pmd"
        buildStatus = sh returnStatus: true, script: "${mvnHome}/bin/mvn clean package -DVERSION=${env.BUILD_NUMBER} "

       } else {

 buildStatus=  sh  returnStatus: true, script:"${mvnHome}/bin/mvn clean package -DVERSION=SNAPSHOT  findbugs:findbugs  -Dfindbugs.maxRank=1 pmd:pmd -DminimumPriority=3"

       }
sh "git rev-parse --short HEAD > .git/commit-id"                        
commitId = readFile('.git/commit-id').trim()
      
     echo commitId
         echo  "BuildStatusPrinting"
         echo  "${buildStatus}"
          if (buildStatus !=0){
if("${env.BRANCH_NAME}".contains("Release")) {
   slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Failed For: ${BRANCH_NAME}",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
}
httpRequest customHeaders: [[name: 'PRIVATE-TOKEN', value: "${GIT_TOKEN}"]], httpMode: 'POST', url: "${GIT_URL}${RMS_GIT_ID}/statuses/${commitId}?state=failed"

       error 'Build Failed'
           }
     
          if(buildStatus==0){
if("${env.BRANCH_NAME}".contains("Release")) {
   slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Success For: ${BRANCH_NAME}",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
}
httpRequest customHeaders: [[name: 'PRIVATE-TOKEN', value: "${GIT_TOKEN}"]], httpMode: 'POST', url: "${GIT_URL}${RMS_GIT_ID}/statuses/${commitId}?state=success"
          }


        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
       


       publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'target/site/jacoco', reportFiles: 'index.html', reportName: 'Code Coverage'])


   if("${env.BRANCH_NAME}".contains("Release")) {

   stage 'Create Package'

    sh "${mvnHome}/bin/mvn assembly:single -DVERSION=${env.BUILD_NUMBER}"

    def groupId = getPomValue(readFile("pom.xml"), "groupId")
    def artifactId = getPomValue(readFile("pom.xml"), "artifactId")
    def version= getPomValue(readFile("pom.xml"), "version")
    def releaseVersion = version.substring(0, version.lastIndexOf("_") + 1) + "${env.BUILD_NUMBER}"
    
stage ' Publish Package'
            long start_time_5 = System.currentTimeMillis();
            long wait_time_5 = 60000;
            long end_time_5 = start_time_5 + wait_time_5;
            def status_5=0
            while (System.currentTimeMillis() < end_time_5 && status_5 != 200 )
        {
            try {

                        nexusArtifactUploader = nexusArtifactUploader  artifactId: "${artifactId}",
                        credentialsId: 'Nexus',
                        file: "target/${artifactId}-${releaseVersion}-dist.zip",
                        groupId: "${groupId}",
                        nexusUrl: '192.168.126.99:8087',
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        repository: 'RMS',
                        type: 'zip',
                        version: "${releaseVersion}"
                    def response_5 = httpRequest  timeout: 30, url: "http://${NEXUS_BASE_URL}/repository/RMS/${groupId}/${artifactId}/${releaseVersion}/${artifactId}-${releaseVersion}.zip", validResponseCodes: '200'
                    status_5 = response_5.status
               }
            catch(Exception e  ){
            sleep(1)
               }
        }
            if (status_5 !=200)
            error 'Nexus Upload Failed'
            
            
  stage 'guineaQA'
    slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Started For: ${BRANCH_NAME} For guineaQA",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
                            
                        echo 'guineaQA Deploying'
    build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "guineaQA"), string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]
    
    
                        sleep(60)
            long start_time_7 = System.currentTimeMillis();
            long wait_time_7 = 120000;
            long end_time_7 = start_time_7 + wait_time_7;
            def status_7=0
            while (System.currentTimeMillis() < end_time_7 && status_7 != 200 )
            {
            try {
            def response = httpRequest  timeout: 30, url: "http://192.168.134.46:8085/RMS", validResponseCodes: '200'
            status_7 = response.status
            }
            catch(Exception e  ){
            sleep(5)
            }

            }
            if (status_7 !=200)
            error 'Application Not Running'
            if(status_7==200){
         slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Success For: ${BRANCH_NAME} For guineaQA",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
        }    


        stage 'TonyBet'
    slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Started For: ${BRANCH_NAME} For QA-Deploy-Ghana",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
                            echo 'Deploying...' 
    echo 'TonyBet QA Deploying'
    build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "tonybet"), string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]
    
    
                        sleep(60)
            long start_time_tonyBet = System.currentTimeMillis();
            long wait_time_tonyBet = 120000;
            long end_time_tonyBet = start_time_tonyBet + wait_time_tonyBet;
            def status_tonyBet=0
            while (System.currentTimeMillis() < end_time_tonyBet && status_tonyBet != 200 )
            {
            try {
            def response = httpRequest  timeout: 30, url: "http://192.168.124.73:8085/RMS", validResponseCodes: '200'
            status_tonyBet = response.status
            }
            catch(Exception e  ){
            sleep(5)
            }

            }
            if (status_tonyBet !=200)
            error 'Application Not Running'
            if(status_tonyBet==200){
     slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Success For: ${BRANCH_NAME} For QA-Deploy-TonyBet",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
    }  
                           
  stage 'QA-Deploy-Lagos-1'
    slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Started For: ${BRANCH_NAME} For QA-Deploy-Lagos-1",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
                            echo 'Deploying...'
                           
    echo 'lagosQA1 Deploying'
   build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "lagosQA1"), string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]
                          //   echo 'Cloud Deploy'
    //build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "aws-demo"),string(name: 'remote', value: "yes"),string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]

                     
                        sleep(60)
            long start_time = System.currentTimeMillis();
            long wait_time = 120000;
            long end_time = start_time + wait_time;
            def status=0
            while (System.currentTimeMillis() < end_time && status != 200 )
            {
            try {
            def response = httpRequest  timeout: 30, url: "http://${LAGOS_QA_LMS1}:${LAGOS_QA_JBOSS4_PORT}/RMS", validResponseCodes: '200'
            status = response.status
            }
            catch(Exception e  ){
            sleep(5)
            }
            
            }
            if (status !=200)
            error 'Application Not Running'
            if(status==200){
		 slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Success For: ${BRANCH_NAME} For QA-Deploy-Lagos-1",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
		}

	  stage 'QA-Deploy-Lagos-2'
    slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Started For: ${BRANCH_NAME} For QA-Deploy-Lagos-2",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
                            
                        echo 'lagosQA2 Deploying'
    build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "lagosQA2"), string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]
    
    
                        sleep(60)
            long start_time_2 = System.currentTimeMillis();
            long wait_time_2 = 120000;
            long end_time_2 = start_time_2 + wait_time_2;
            def status_2=0
            while (System.currentTimeMillis() < end_time_2 && status_2 != 200 )
            {
            try {
            def response = httpRequest  timeout: 30, url: "http://${LAGOS_QA_LMS2}:${LAGOS_QA_JBOSS4_PORT}/RMS", validResponseCodes: '200'
            status_2 = response.status
            }
            catch(Exception e  ){
            sleep(5)
            }

            }
            if (status_2 !=200)
            error 'Application Not Running'
            if(status_2==200){
		 slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Success For: ${BRANCH_NAME} For QA-Deploy-Lagos-2",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
		}
     
    stage 'QA-Deploy-Zimbabwe'
    slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Started For: ${BRANCH_NAME} For QA-Deploy-Zimbabwe",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
                            echo 'Deploying...'
                           
    echo 'Zimbabwe QA Deploying'
   build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "zimQA"), string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]
                          //   echo 'Cloud Deploy'
    //build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "aws-demo"),string(name: 'remote', value: "yes"),string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]

                     
                        sleep(60)
            long start_time_zim = System.currentTimeMillis();
            long wait_time_zim = 120000;
            long end_time_zim = start_time_zim + wait_time_zim;
            def status_zim=0
            while (System.currentTimeMillis() < end_time_zim && status_zim != 200 )
            {
            try {
            def response = httpRequest  timeout: 30, url: "http://${ZIM_QA}:${ZIM_QA_JBOSS4_PORT}/RMS", validResponseCodes: '200'
            status_zim = response.status
            }
            catch(Exception e  ){
            sleep(5)
            }
            
            }
            if (status_zim !=200)
            error 'Application Not Running'
            if(status_zim==200){
		 slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Success For: ${BRANCH_NAME} For QA-Deploy-Zimbabwe",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
		} 
    
     stage 'QA-Deploy-Safaribet'
        slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Started For: ${BRANCH_NAME} For QA-Deploy-Safaribet",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
                            echo 'Deploying...'

        echo 'Safari Bet QA Deploying'
        build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "Safaribet-UAT"), string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]
    
    
                        sleep(60)
            long start_time_SB = System.currentTimeMillis();
            long wait_time_SB = 120000;
            long end_time_SB = start_time_SB + wait_time_SB;
            def status_SB=0
            while (System.currentTimeMillis() < end_time_SB && status_SB != 200 )
            {
            try {
            def response = httpRequest  timeout: 30, url: "http://192.168.134.27:8180/RMS", validResponseCodes: '200'
            status_SB = response.status
            }
            catch(Exception e  ){
            sleep(5)
            }

            }
            if (status_SB !=200)
            error 'Application Not Running'
            if(status_SB==200){
		 slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Success For: ${BRANCH_NAME} For QA-Deploy-Safaribet",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
		} 

 stage 'QA-Deploy-Ghana'
    slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Started For: ${BRANCH_NAME} For QA-Deploy-Ghana",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
                            echo 'Deploying...' 
    echo 'Ghana QA Deploying'
    build job: 'AnsibleTestJob', parameters: [string(name: 'target', value: "Ghana-QA"), string(name: 'NEXUS_URL', value: "${NEXUS_BASE_URL}"), string(name: 'RepoName', value: "RMS"), string(name: 'GroupId', value: "${groupId}"), string(name: 'ArtifactId', value: "${artifactId}"), string(name: 'ReleaseVersion', value: "${releaseVersion}"), string(name: 'FileName', value: "${artifactId}-${releaseVersion}.zip")]
    
    
                        sleep(60)
            long start_time_SC = System.currentTimeMillis();
            long wait_time_SC = 120000;
            long end_time_SC = start_time_SC + wait_time_SC;
            def status_SC=0
            while (System.currentTimeMillis() < end_time_SC && status_SC != 200 )
            {
            try {
            def response = httpRequest  timeout: 30, url: "http://192.168.134.14:8085/RMS", validResponseCodes: '200'
            status_SC = response.status
            }
            catch(Exception e  ){
            sleep(5)
            }

            }
            if (status_SC !=200)
            error 'Application Not Running'
            if(status_SC==200){
     slackSend color: "#439FE0", message: "Build Number:${BUILD_NUMBER} Deployment Success For: ${BRANCH_NAME} For QA-Deploy-Ghana",  teamDomain: 'skilrock', token: 'PJZoglka3a5W4T6fZUfLwWfE'
    }
    
}
    
    
    /* stage 'FunctionalTest'
        node ("Windows") {

        build job: 'Functional_Tests_Lagos', parameters: [string(name: 'FeatureTag', value: '@Api'), string(name: 'ExecutionPlatform', value: 'Api'), string(name: 'TestCaseTag', value: '@Lagos')]
} */
}


@NonCPS
def getPomValue(text, element) {
  def matcher = text =~ '<' + element + '>(.+)</' + element + '>'
  matcher ? matcher[0][1] : null
}
