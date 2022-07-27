import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
const firebaseConfig = {
  apiKey: "AIzaSyDl-RZxnRdxItcLS9XV-aizTuOwCHsdykA",
  authDomain: "e-shopping-74a1e.firebaseapp.com",
  projectId: "e-shopping-74a1e",
  storageBucket: "e-shopping-74a1e.appspot.com",
  messagingSenderId: "253186501415",
  appId: "1:253186501415:web:d82d908a662827e7061702",
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

const provider = new GoogleAuthProvider();

export { auth, provider, signInWithPopup };
