"use strict";(self.webpackChunkjavascript=self.webpackChunkjavascript||[]).push([[87],{3087:function(e,t,r){r.r(t),r.d(t,{RegisterPage:function(){return w}});var a=r(7294),n=r(5005),o=r(6025),l=r(9669),s=r.n(l),c=r(7577),u=r(9942),i=r(6968);function m(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,a)}return r}function p(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?m(Object(r),!0).forEach((function(t){d(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):m(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function d(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function f(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var r=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=r){var a,n,o=[],l=!0,s=!1;try{for(r=r.call(e);!(l=(a=r.next()).done)&&(o.push(a.value),!t||o.length!==t);l=!0);}catch(e){s=!0,n=e}finally{try{l||null==r.return||r.return()}finally{if(s)throw n}}return o}}(e,t)||function(e,t){if(e){if("string"==typeof e)return b(e,t);var r=Object.prototype.toString.call(e).slice(8,-1);return"Object"===r&&e.constructor&&(r=e.constructor.name),"Map"===r||"Set"===r?Array.from(e):"Arguments"===r||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(r)?b(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function b(e,t){(null==t||t>e.length)&&(t=e.length);for(var r=0,a=new Array(t);r<t;r++)a[r]=e[r];return a}var w=function(e){var t=f((0,a.useState)({username:"",password:null,password2:null,email:""}),2),r=t[0],l=t[1],m=f((0,a.useState)(),2),b=m[0],w=m[1],y=f((0,a.useState)(),2),g=y[0],h=y[1],v=f((0,a.useState)(!1),2),j=v[0],E=v[1],O=function(e){l(p(p({},r),{},d({},e.target.name,e.target.value))),"password"!==e.target.name&&"password2"!==e.target.name||h()};return a.createElement("div",{className:"u-text-center"},a.createElement("div",{className:"u-box-shadow"},a.createElement("div",{className:"row justify-content-center"},a.createElement("div",{className:"col-lg-4 pb-3 u-box-shadow"},a.createElement(o.Z,{className:"text-center"},a.createElement(o.Z.Header,{className:"bg-dark text-white"},"Rejestracja"),a.createElement(o.Z.Body,null,a.createElement("form",{onSubmit:function(e){e.preventDefault(),r.password===r.password2?(E(!0),s().post("".concat((0,u.AC)(),"/auth/signup"),r).then((function(e){w(e.data.username),E(!1)})).catch((function(e){h(e.response.data),E(!1)}))):h("Wprowadź jednakowe hasła")}},a.createElement(c.K,{label:"Login",name:"username",handleChange:O,big:!0,value:r.username,required:!0,autoComplete:"new-password"}),a.createElement(c.K,{label:"Email",name:"email",handleChange:O,big:!0,value:r.email,required:!0,type:"email",autoComplete:"new-password"}),a.createElement(c.K,{label:"Hasło",name:"password",handleChange:O,big:!0,value:r.password,type:"password",required:!0,autoComplete:"new-password"}),a.createElement(c.K,{label:"Powtórz hasło",name:"password2",handleChange:O,big:!0,value:r.password2,type:"password",required:!0}),b&&a.createElement("p",null,"Zarejestrowano użytkownika: ".concat(b)),g&&a.createElement("p",null,"".concat(g,".")),j&&a.createElement("div",null,a.createElement(i.Z,{animation:"border",variant:"secondary",size:"lg"})),a.createElement(n.Z,{className:"px-4 mt-2",variant:"success",type:"submit"},"Zarejestruj"))),a.createElement(o.Z.Footer,{className:"text-muted"},"Masz konto -",a.createElement("a",{href:"login"}," zaloguj się")))))))};t.default=w}}]);
//# sourceMappingURL=87.js.map