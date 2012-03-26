#/bin/sh

export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home

cd ~/Dropbox/Karthik\'s\ Document/EclipseWorkspace/AWS_TravelLog
~/Downloads/apache-maven-2.2.1/bin/mvn compile war:war
rm -rf ~/Downloads/apache-tomcat-7.0.23/webapps/Signle2-1.0.0
cp target/Signle2-1.0.0.war ~/Downloads/apache-tomcat-7.0.23/webapps/
cd ~/Downloads/apache-tomcat-7.0.23/bin
./shutdown.sh
./startup.sh

