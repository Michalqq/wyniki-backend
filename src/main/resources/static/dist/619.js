"use strict";(self.webpackChunkjavascript=self.webpackChunkjavascript||[]).push([[619],{1619:function(e,t,r){r.r(t),r.d(t,{ResetPasswordPage:function(){return y}});var a=r(7294),n=r(5005),o=r(6025),s=r(9669),c=r.n(s),l=(r(4039),r(7577)),u=r(9942),i=r(6968),p=r(6974);function d(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,a)}return r}function m(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?d(Object(r),!0).forEach((function(t){f(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):d(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function f(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function w(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var r=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=r){var a,n,o=[],s=!0,c=!1;try{for(r=r.call(e);!(s=(a=r.next()).done)&&(o.push(a.value),!t||o.length!==t);s=!0);}catch(e){c=!0,n=e}finally{try{s||null==r.return||r.return()}finally{if(c)throw n}}return o}}(e,t)||function(e,t){if(e){if("string"==typeof e)return b(e,t);var r=Object.prototype.toString.call(e).slice(8,-1);return"Object"===r&&e.constructor&&(r=e.constructor.name),"Map"===r||"Set"===r?Array.from(e):"Arguments"===r||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(r)?b(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function b(e,t){(null==t||t>e.length)&&(t=e.length);for(var r=0,a=new Array(t);r<t;r++)a[r]=e[r];return a}var y=function(e){var t=w((0,a.useState)({token:(0,p.TH)().search,username:"",password:"",password2:""}),2),r=t[0],s=t[1],d=w((0,a.useState)(),2),b=d[0],y=d[1],h=w((0,a.useState)(!1),2),v=h[0],g=h[1],j=function(e){s(m(m({},r),{},f({},e.target.name,e.target.value)))};return a.createElement("div",{className:"u-text-center"},a.createElement("div",{className:"u-box-shadow"},a.createElement("div",{className:"row justify-content-center"},a.createElement("div",{className:"col-lg-4 pb-3 u-box-shadow"},a.createElement(o.Z,{className:"text-center"},a.createElement(o.Z.Header,{className:"bg-dark text-white"},"Ustaw nowe hasło"),a.createElement(o.Z.Body,null,a.createElement("form",{onSubmit:function(e){e.preventDefault(),r.password===r.password2?(g(!0),y(),c().post("".concat((0,u.AC)(),"/auth/updatePassword"),r).then((function(e){200===e.status?y("Hasło zostało zaktualizowane. Proszę się zalogować"):y(e.data),g(!1)})).catch((function(e){y(e.response.data),g(!1)}))):y("Wprowadź jednakowe hasła")}},a.createElement(l.K,{label:"Hasło",name:"password",handleChange:j,big:!0,value:r.password,type:"password",required:!0,autoComplete:"new-password"}),a.createElement(l.K,{label:"Powtórz hasło",name:"password2",handleChange:j,big:!0,value:r.password2,type:"password",required:!0,autoComplete:"new-password"}),b&&a.createElement("p",null,"".concat(b)),v&&a.createElement("div",null,a.createElement(i.Z,{animation:"border",variant:"secondary",size:"lg"})),a.createElement(n.Z,{className:"px-4 mt-2",variant:"success",type:"submit"},"Zapisz nowe hasło"))),a.createElement(o.Z.Footer,{className:"text-muted"},"Masz konto -",a.createElement("a",{href:"login"}," zaloguj się")))))))};t.default=y}}]);
//# sourceMappingURL=619.js.map