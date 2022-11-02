# Sharepoint-prototype
<h3>Requirements</h3>
* Java 8 or later
* Maven
* Any operating system
* Running Sharepoint application

<h3>Usage:</h3>
<h4>Required arguments (positional):</h4>
<ul>
    <li>site    : <em>Sharepoint site url</em>  </li>
    <li>username: <em>Sharepoint username</em>  </li>
    <li>password: <em>Sharepoint password</em>   </li>
    <li>domain  : <em>Sharepoint domain</em>   </li>
</ul>
<h4>Optional arguments (any order):</h4>
<ul>
<li>--skip=       :  <em>Skip a Sharepoint object. Options include 'subsites','lists','folders','files'. : You may skip multiple objects. Look at example 3</em></li>
<li>--output=     : <em>Do you want output files? Options include 'true', 'false' (default is false)</em></li>
<li>--no-download : <em>Do you want to fetch discovered files? Default is false, add argument to make true</em></li>
<li>--filter-conf=: <em>Include a filter (date-range & order): Path to the .json file is required</em></li>
</ul>

EXAMPLES:
1. Collect all site data (includes file download)  
```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain```
2. Skip subsites, don't download files  
   ```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=subsites --no-download```
3. Skip subsites and folders. (As a result, no files get downloaded because we're skipping folders)  
   ```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=subsites,folders```
4. Skip lists, apply a configuration  
   ```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=lists --filter-conf=myFilter.json```  
   * Examples of json configs are in the config-examples folder
   * You can apply multiple filters, just specify the values in the json config  

For any further questions, please contact sir Elijah Reyes (elijah.reyes@hitachivantarafederal.com) or please visit https://www.odata.org/documentation/odata-version-2-0/uri-conventions/#OrderBySystemQueryOption to review odata query options