# TwitterComplainer

This application was made with the objective to 'auto complain' on twitter (I'll curse a lot on twitter), but in the end, you can register some whatever message to publish in give period (cron).

## Requirements

- <img src="https://img.shields.io/badge/Java-11-blue"></img>
- <img src="https://img.shields.io/badge/Kotlin-1.5.30-blue"></img>
- <img src="https://img.shields.io/badge/MongoDB-4.4-blue"></img>
- An account on developer.twitter [Developer Twitter](https://developer.twitter.com/)

## Technologies

- <img src="https://img.shields.io/badge/Springboot-2.5.4-blue"></img>
- <img src="https://img.shields.io/badge/Kotlin Coroutines-1.5.2-blue"></img>
- <img src="https://img.shields.io/badge/Twittered-2.8-blue"></img> from [twittered](https://github.com/redouane59/twittered) 2.8
- <img src="https://img.shields.io/badge/Mockk-1.12-blue"></img>
- <img src="https://img.shields.io/badge/Kotest-4.6.2-blue"></img>

## Usage
>I use docker to initialize the apps, so, I have a file named `docker-compose.yaml` in the project with `mongo`, `mongo-express` and `app` configurations
The `mongo-express` is the admin page for management of the database, so let's run:

<details>
  <summary>Database Configuration</summary>
  
  - First, let's initialize the database

  ```
  $ docker-compose up -d mongo mongo-express
  ```

  - The mongo-express will be exposed ant port `8081`, so you can access by `localhost:8081`
  - Use the login & password configured on docker-compose
    - default:
      - login: root
      - password: admin
  - Create a database called `twitter` and a collection called `posts`
</details>

<details>
  <summary>Twitter Client Configuration</summary>
  
  - After registering on Twitter Developer, you have to export some environmnet variables to the app can post on twitter
  - Tokens for your @User on Twitter
    - TWITTER_TOKEN
    - TWITTER_TOKEN_SECRET
  - Keys for the application
    - TWITTER_API_KEY
    - TWITTER_API_SECRET_KEY
</details>

<details>
  <summary>Single Tweet</summary>

  - In the posts collection, create the documents with your message
  
  ```javascript
  {
      _id: ObjectId('613e13761204d5ec25f31bde'),
      cron: '0 30 8 * * ?',
      texts: [
          'Here a text to post on twitter'
      ]
  }
  ```

</details>

<details>
  <summary>Mutiple Tweets</summary>

  - In the posts collection, create the documents with your messages
  ```javascript
  {
      _id: ObjectId('613e13761204d5ec25f31bde'),
      cron: '0 30 8 * * ?',
      texts: [
          'First tweet',
          'Second tweet linked to the first',
          'Third tweet linkked to the second'
      ]
  }
  ```

</details>

- Then, you can start the app by `docker-compose up app`

## Important
- The texts have the maximum of `280` characteres per tweet
- Everytime you manage your posts, you have to restart the app, because it create the cron just on startup
- The `Cron` use the [Spring Cron Expression](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/support/CronExpression.html)

## For the future
- [ ] Implement a way to manage the posts without restart the app
   - Endpoint or MessageQueue

