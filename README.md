## Calculator

### URL online

https://pedmas.herokuapp.com/calculus?query=query

The Heroku container spins down after 30 minutes of inactivity; for this reason, the first query made in a session will take a few seconds.

### Deployment

This app was deployed on Heroku, roughly following these instructions [here](https://devcenter.heroku.com/articles/getting-started-with-clojure#deploy-the-app).

```console
$ heroku login
$ heroku apps:create pedmas --region eu
$ git push heroku master
```

Deployment uses a standard Heroku Procfile with a script that produces a JAR file.

To run locally on default port 3000, run `lein run`. Optionally, to specify a port, add the number as an argument (e.g. `lein run 5000`)
