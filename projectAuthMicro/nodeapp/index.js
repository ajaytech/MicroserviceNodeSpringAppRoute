const express = require("express");
const hdb = require("hdb");
const xsenv = require("@sap/xsenv");
const passport = require("passport");
const { JWTStrategy } = require("@sap/xssec");

const xsenvObj = xsenv.getServices({ hana: { tag: "database" } });
const xsenvObjXSUAA = xsenv.getServices({ uaa: { tag: "xsuaa" } });
const port = process.env.port || 8080;
const app = express();

passport.use(new JWTStrategy(xsenvObjXSUAA.uaa));

app.use(passport.initialize());
app.use(passport.authenticate("JWT", { session: false }));

const hdiConfig = {
  host: xsenvObj.hana.host,
  port: xsenvObj.hana.port,
  user: xsenvObj.hana.hdi_user,
  password: xsenvObj.hana.hdi_password,
  useTLS: true
};

const client = hdb.createClient(hdiConfig);

client.on("error", function(err) {
  console.error("Network connection error", err);
});

app.get("/Student", checkRole, getDataStudent);
app.get("/Student/:id", checkRole, getDataStudentWithID);

function checkRole(req, res, next) {
  if (req.authInfo.checkLocalScope("readDataAjay")) {
    return next();
  } else {
    res.status(403).end("Forbidden");
  }
}

function getDataStudent(req, res) {
  console.log('select * from "' + xsenvObj.hana.schema + '"."STUDENT"');

  client.connect(function(err) {
    if (err) {
      console.error("Connect error", err);
      res.status(500).end("Internal Server Error in DB Connection");
    }
    client.exec(
      'select * from "' + xsenvObj.hana.schema + '"."STUDENT"',
      function(err, rows) {
        client.end();
        if (err) {
          console.error("Execute error:", err);
          res.status(500).end("Internal Server Error in Getting Data");
        }
        console.log("Results:", rows);
        res.send(rows);
      }
    );
  });
}

function getDataStudentWithID(req, res) {
  //console.log(req.params.id);

  if (typeof req.params.id !== "undefined" && req.params.id) {
    console.log(
      'select * from "' +
        xsenvObj.hana.schema +
        '"."STUDENT" where id=' +
        req.params.id
    );

    client.connect(function(err) {
      if (err) {
        console.error("Connect error", err);
        res.status(500).end("Internal Server Error in DB Connection");
      }
      client.exec(
        'select * from "' +
          xsenvObj.hana.schema +
          '"."STUDENT" where id = ' +
          req.params.id,
        function(err, rows) {
          client.end();
          if (err) {
            console.error("Execute error:", err);
            res.status(500).end("Internal Server Error in Getting Data");
          }
          console.log("Results:", rows);
          res.send(rows);
        }
      );
    });
  } else {
    res.status(500).end("Internal Server Error - No ID Passed");
  }
}

app.use("/", express.static("ui5AppStatic/"));
app.listen(port);
