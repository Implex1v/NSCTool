# web
## Development

To run the development server run:
```shell
npm run dev
```

You can start db and keycloak in docker by running:
```shell
docker-compose up db keycloak
```
Sadly, you need to run the API locally without docker, because the issuer `iss` is wrong.
This is caused by the fact, that the web is running locally and the other services in docker. 

## CSS & HTML

We are using Bootstrap as a CSS Framework.
For general Bootstrap documentation see https://getbootstrap.com/docs/5.2/getting-started/introduction/ and for react specific see https://react-bootstrap.github.io/.

For icons use https://fontawesome.com/docs/web/use-with/react/.

## Resources

* https://nextjs.org/learn
* https://next-auth.js.org/getting-started/example
* https://reactjs.org/tutorial/tutorial.html
* https://getbootstrap.com/docs/5.2/getting-started/introduction/
* https://react-bootstrap.github.io/
* https://fontawesome.com/docs/web/use-with/react/