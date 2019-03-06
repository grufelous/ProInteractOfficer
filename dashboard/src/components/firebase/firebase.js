import * as firebase from 'firebase';

const config = {
  apiKey: "AIzaSyAqIXUp_zWlp9LSyut81I38o8eePhxhx0g",
  authDomain: "prointeract-af821.firebaseapp.com",
  databaseURL: "https://prointeract-af821.firebaseio.com",
  projectId: "prointeract-af821",
  storageBucket: "prointeract-af821.appspot.com",
  messagingSenderId: "372920954750"
};

firebase.initializeApp(config);

const db = firebase.database();

export {firebase, db as default};
