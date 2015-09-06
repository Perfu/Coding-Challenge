AppDirect Coding Challenge
===============================

This application is use to demonstrate the integration of some AppDirect's APIs which are :

 * Subscription
 * User assignement
 * Two-legged Oauth using signpost
 * Login with OpenId using openid4java

By default, It use a PostGreSql database. In SQL folder, there is a SQL script to create the needed tables.

Ui is only raw HTML. there is no css.

To install the application, follow these steps :

1. Register as a developer at AppDirect: http://info.appdirect.com/developer and read their developer documentation at this address: 
http://info.appdirect.com/developers/docs/api_integration/api_overview

2. Add the following environment variable :
    * CONSUMER_KEY (Can be obtain on your appdirect product's configuration)
    * SECRET_KEY (same as above)
    * DATABASE_URL = [database type]://[username]:[password]@[host]:[port]/[database name]. Example postgres://foo:foo@heroku.com:5432/hellodb
3. Clone this repository to your local system
4. Run 'mvn clean package'
5. Deploy it on a web server like tomcat