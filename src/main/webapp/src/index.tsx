import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import {HashRouter} from "react-router-dom";
// Do not remove import, it sets up the proxies
// import SetupProxy from "./setupProxy"

ReactDOM.render(
  // HashRouter because otherwise we get Whitelabel error pages from Spring since there's no mapping once we f.e. refresh a /clients page
  <HashRouter>
    <App />
  </HashRouter>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
