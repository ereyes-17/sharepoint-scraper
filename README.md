# Sharepoint-prototype
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
4. Skip lists, apply AfterDate_Modified filter to files (.json config included after command)  
   ```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=lists --filter-conf=myFilter.json```  
  myFilter.json content:
```{
{
     "filter": {
       "lists": {
         "BeforeDate_itemModified" : null,
         "AfterDate_itemModified": null
       },
        "folders": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": null
        },
        "files": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": "2022-10-24T23:59:59"
        }
     },
     "orderBy": {
     }
}
 ```
5. Collect all data, apply date range filter to lists (.json config included after command)  
   ```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --filter-conf=myFilter.json```  
   myFilter.json content: 
```{
{
     "filter": {
       "lists": {
         "BeforeDate_itemModified" : "2022-10-24T23:59:59",
         "AfterDate_itemModified": "2022-10-22T23:59:59"
       },
        "folders": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": null
        },
        "files": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": null
        }
     }
     "orderBy": {
     }
}
 ```
 
6. Collect all data, apply date range filter to lists & order folders by ItemCount (.json config included after command)  
   ```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --filter-conf=myFilter.json```  
myFilter.json content: 
```{
{
     "filter": {
       "lists": {
         "BeforeDate_itemModified" : "2022-10-24T23:59:59",
         "AfterDate_itemModified": "2022-10-22T23:59:59"
       },
        "folders": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": null
        },
        "files": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": null
        }
     }
     "orderBy": {
        "folders": ["ItemCount"]
     }
}
 ```
7. Skip subsites, order folders by ItemCount, Title & order lists by Title (.json config included after command)  
   ```java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --filter-conf=myFilter.json --skip=subsites```  
   myFilter.json content:
```{
{
     "filter": {
       "lists": {
         "BeforeDate_itemModified" : null,
         "AfterDate_itemModified": null
       },
        "folders": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": null
        },
        "files": {
          "BeforeDate_Modified" : null,
          "AfterDate_Modified": null
        }
     }
     "orderBy": {
        "lists": ["Title"],
        "folders": ["ItemCount,Title"]
     }
}
 ```
The <code>orderBy</code> field may also have an <code>arrangement</code> property to return the result in <em>ascending</em> or <em>descending</em> order denoted by <strong>"asc"</strong> or <strong>"desc"</strong> respectively.  
Note that you can apply multiple filters, just specify the values in the json config.
For any further questions, please contact sir Elijah Reyes (elijah.reyes@hitachivantarafederal.com)