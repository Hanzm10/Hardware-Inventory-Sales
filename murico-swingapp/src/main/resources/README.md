# Murico User Documentation

Welcome! This is a dummy 'client' for a hardware inventory and 'sales' system. This Java application connects
to a database through `JDBC`. The database used is `MySQL`. Therefore;
for testing this application, the following are prerequisites to run the app:

1. `MySQL` - This can be downloaded in [MySQL Community Downloads](https://dev.mysql.com/downloads/)

## Authentication

For testing purposes, the application has two accounts as an admin. Try one out!
Just input these credentials in the login page. Have at it!

```
username = aaron
password = Seed@123
```

## Application Startup

If you are seeing this, that means something went wrong at app startup. Please check these first:

1. Database is not installed:
 - Please download MySQL first
2. Database is not set up:
 - Please set up MySQL first
3. The registered database credentials for this application do not match with the database:
 - Please set up your database server's credentials to match with what the app is using. Refer to the **Database Credentials** section
	
## Database Credentials

As of `v1.0.0`, the application is using these configurations for local databases:

```properties
db.url       = jdbc:mysql://localhost:3306/murico
db.user      = root
db.password  = 12345
db.name      = murico
```

Please create a database server in your downloaded `MySQL` with the aforementioned credentials to use the application.
We thank you for your understanding!

## Walkthrough

Below is a walk through of what you could do. The tutorial below uses Windows 10, so there might be conflicts if you're on a different Operating System.

1. Go to [MySQL Community Downloads](https://dev.mysql.com/downloads/)

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747847889/murico/hgprvma9ncm8tqolnmgz.jpg" alt="" width=600>

2. Click MySQL Installer for Windows

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747848128/murico/useewmrpieykaqicciep.png" alt="" width=600>

3. Once on the page, choose either of the two. The web community will require for an internet connection when the installation `wizard`
installs your files, while the latter will not. Choose one that fits your needs as they are the same. Once you click `download`,
you will be redirected to:

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747848261/murico/pvyl3gpupprfh4axcs1r.jpg" alt="" width=600>

4. Click *'No thanks, just start my download'*. You should be able to see this in your browser's downloads tab

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747848355/murico/tuudptddtejhfirrvgl4.jpg" alt="" width=400>

5. Wait for download and follow the wizard's installation steps. The instructions in the wizard should be sufficient enough.
6. Please download both *MySQL Server* and *MySQL Workbench*.
7. When installing the *MySQL Server*, this should appear:

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747848735/murico/itpsff8k0cj3gm8uxoka.jpg" alt="" width=600>

8. The database configurations mentioned in **Database Credentials** should already be the default of the server:
 - PORT `3306`
 - USER `root`
9. *MySQL* will then prompt you to enter a password for `root`. Please enter the password above, which is `12345`.
10. After everything is set up, you can open the *MySQL Workbench*. Your view should look like:

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747849119/murico/tyixasizbqj1ye7z6fan.jpg" alt="" width=600>

11. Right-click on the sidebar under the keyowrd **SCHEMAS** and a context menu should pop up.

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747849265/murico/q6ebtqfmsbtsm2dyx1km.jpg" alt="" width=600>

12. Once clicked, a panel should appear where you can create a new schema. It should look like:

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747849354/murico/gg3sbf1gjgw497nohztj.jpg" alt="" width=600>

13. On the text input with the label *Name*, replace the value `new_schema` with the aforementioned `db.name` above, which is
`murico`.
14. Click apply. A window like this should then appear:

<img src="https://res.cloudinary.com/dazgf1omh/image/upload/v1747849461/murico/tvgh6a0ys0bgmrestunq.jpg" alt="" width=600>

15. Click apply and the `murico` schema or database should then be applied.
16. Close the *MySQL Workbench* application.
17. Try to re-run the *Muric* application.

## Conclusion

Thank you for reading! May you have a wonderful day and contact us if there are problems. Plus points in documentation? :) Thank you and god bless!