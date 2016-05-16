## AndroidSimpleContentProvider

> A simple example of Content Provider with SQLite.


## Main Files

**[DBHelper.java](https://github.com/luizfelippe/AndroidSimpleContentProvider/blob/master/src/main/java/br/com/zelius/simplecontentprovider/data/DBHelper.java)** 
Just for setting up the SQLite database.


**[DBContract.java](https://github.com/luizfelippe/AndroidSimpleContentProvider/blob/master/src/main/java/br/com/zelius/simplecontentprovider/data/DBContract.java)**
Here we define the SQL Tables and Columns and then the URIs which our Content Provider will comunicate to.

In this example I created a simple Table of Alarms so we can do basic CRUD.


**[DBProvider.java](https://github.com/luizfelippe/AndroidSimpleContentProvider/blob/master/src/main/java/br/com/zelius/simplecontentprovider/data/DBProvider.java)**
That's where the `magic` happens.

This class extends the ContentProvider class, and then we override it's methods so we can make our Content Provider work.


> PS: Don't forget about the **provider** tag in our AndroidManifest, which makes the DBProvider our APP's default Content Provider.
