# LinkedTree

This application is the server for the LinkedTree application (see previous folders).

Its goal is to give access to the database services, such as create a tree and send a message.

## Installation

Make sure you have [Node.js](http://nodejs.org/) installed on your server, and do the following :

```sh
$ git clone https://github.com/khalidmr/PdcHybrid
$ cd PdcHybrid/server
$ npm install
$ npm start
```

The server should now be running on [localhost:5000](http://localhost:5000/).

Remember to install the modules mongoose, express and body-parser with :
```sh
$ npm install [module name]
```

## Using the server

The server uses HTTP request to execute its services. To call a service, juste send a POST request to the correct URL with all parameters required in JSON format.

To see example and a list of all the callable services, please read [the route description](./routes.pdf).

## Description of folders

The server is splitted in three folders : public, node_modules and services.
 - Public folder contains public informations about nodejs ;
 - Node_modules folder contains npm modules used by the server ;
 - Services folder contains all files used by the server to complete services

 ### Services folder

 In this folder we can find four other folders :
  - database : contains the file that define and launch the database connection, and the data model
  - network : contains all the services relative to Trees and Messages
  - route : contain the router of the server, i.e the file that call the services depending of the URL
  - utils : the habitual utils folder with algorithms and support stuff
  


