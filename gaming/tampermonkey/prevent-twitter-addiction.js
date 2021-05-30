// ==UserScript==
// @name         Prevent twitter addiction
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Prevent twitter addiction
// @author       Johannes Qvarford
// @match        https://twitter.com/*
// @icon         https://www.google.com/s2/favicons?domain=twitter.com
// @grant        none
// ==/UserScript==

(function() {
    'use strict';
    history.pushState = null;
    // Your code here...
})();