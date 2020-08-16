## Calculator



### Deployment

This app was deployed on Heroku, roughly following these instructions [here](https://devcenter.heroku.com/articles/getting-started-with-clojure#deploy-the-app).

```console
heroku login
heroku apps:create pedmas --region eu
git push heroku master
```


To run locally on default port 3000, run `lein run`. Optionally, to specify a port, add the number as an argument (e.g. `lein run 5000`)
