## Docker

### App

`docker build -t geniot/rni:latest -f Dockerfile .`

`docker rm -f rni-mvp`

`docker run -p 22222:8989 --name rni-mvp -d geniot/rni:latest`

### Database
`docker rm -f rni-mvp-postgres`

`docker run --name rni-mvp-postgres -e POSTGRES_USER=rni-mvp -e POSTGRES_PASSWORD=rni-mvp -e POSTGRES_DB=rni-mvp -p 33333:5432 -d postgres`

#### No Space Left On Device

`docker system prune --volumes`



