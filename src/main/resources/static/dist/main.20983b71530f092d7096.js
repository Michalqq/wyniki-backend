"use strict";(self.webpackChunkjavascript=self.webpackChunkjavascript||[]).push([[688],{1688:function(e,t,n){n.r(t),n.d(t,{default:function(){return B}});var r=n(7294),a=n(9669),l=n.n(a),i=n(496),o=n(6974),c=n(9661),s=n(6668),u=n(9942),d=n(3992),m=n(7625),f=n(1436),y=n(9952);function v(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var n=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=n){var r,a,l=[],i=!0,o=!1;try{for(n=n.call(e);!(i=(r=n.next()).done)&&(l.push(r.value),!t||l.length!==t);i=!0);}catch(e){o=!0,a=e}finally{try{i||null==n.return||n.return()}finally{if(o)throw a}}return l}}(e,t)||function(e,t){if(e){if("string"==typeof e)return g(e,t);var n=Object.prototype.toString.call(e).slice(8,-1);return"Object"===n&&e.constructor&&(n=e.constructor.name),"Map"===n||"Set"===n?Array.from(e):"Arguments"===n||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?g(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function g(e,t){(null==t||t>e.length)&&(t=e.length);for(var n=0,r=new Array(t);n<t;n++)r[n]=e[n];return r}var p=function(e){var t=v((0,r.useState)([]),2),n=t[0],a=t[1],o=v((0,r.useState)(!0),2),s=o[0],g=o[1],p=function(){l().get("".concat((0,u.AC)(),"/penalty/getPenalties?eventId=").concat(e.eventId)).then((function(e){a(e.data),g(!1)}))};(0,r.useEffect)((function(){p()}),[]);var b=(0,r.useMemo)((function(){return[{width:"20%",id:"team",Header:"Załoga",disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(r.Fragment,null,r.createElement("div",{className:"py-1 px-2 mx-1 d-grid"},r.createElement(d.M,{value:e.row.original.number})),r.createElement("div",{className:"px-1 mx-1 d-grid"},r.createElement(c.Ur,{team:e.row.original})))}},{width:"70%",id:"penalty",Header:"Czas - Powód - OS/PS",disableFilters:!0,disableSortBy:!0,Cell:function(t){return r.createElement("table",{className:"font14"},t.row.original.penalties.map((function(t){return r.createElement(r.Fragment,null,r.createElement("tr",null,r.createElement("td",{className:"text-left fw-bolder",style:{width:"20%"}},"Taryfa"!==t.description?t.penaltySec+" s":""),r.createElement("td",{className:"text-left px-3",style:{width:"45%"}},t.description),r.createElement("td",{className:"text-left px-3",style:{width:"40%"}},t.name),r.createElement("td",{style:{width:"15%"}},e.referee?r.createElement(m.G,{icon:f.nYk,onClick:function(){return n=t.penaltyId,void l().post("".concat((0,u.AC)(),"/penalty/removePenalty?penaltyId=").concat(n),{},{headers:(0,y.Z)()}).then((function(t){e.onRemove(),p()}));var n},title:"Usuń kare"}):r.createElement(r.Fragment,null))))})))}}]}),[e.referee]);return r.createElement(i.Z,{columns:b,data:n,pageCount:3,isLoading:s,isFooter:!1,isHeader:!0,cursor:"pointer",manualPagination:!0})};function b(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var n=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=n){var r,a,l=[],i=!0,o=!1;try{for(n=n.call(e);!(i=(r=n.next()).done)&&(l.push(r.value),!t||l.length!==t);i=!0);}catch(e){o=!0,a=e}finally{try{i||null==n.return||n.return()}finally{if(o)throw a}}return l}}(e,t)||function(e,t){if(e){if("string"==typeof e)return E(e,t);var n=Object.prototype.toString.call(e).slice(8,-1);return"Object"===n&&e.constructor&&(n=e.constructor.name),"Map"===n||"Set"===n?Array.from(e):"Arguments"===n||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?E(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function E(e,t){(null==t||t>e.length)&&(t=e.length);for(var n=0,r=new Array(t);n<t;n++)r[n]=e[n];return r}var h=function(e){var t=b((0,r.useState)([]),2),n=t[0],a=t[1],o=b((0,r.useState)(!0),2),s=o[0],v=o[1],g=function(){l().get("".concat((0,u.AC)(),"/penalty/getDisqualifications?eventId=").concat(e.eventId)).then((function(e){a(e.data),v(!1)}))};(0,r.useEffect)((function(){g()}),[]);var p=(0,r.useMemo)((function(){return[{width:"5%",id:"nr",Header:"Nr",accessor:function(e){return e.number},disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(d.M,{value:e.value})}},{width:"20%",id:"team",Header:"Załoga",disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(c.Ur,{team:e.row.original})}},{width:"60%",id:"penalty",Header:"Powód - OS/PS",disableFilters:!0,disableSortBy:!0,Cell:function(t){return r.createElement("table",{className:"font14"},t.row.original.penalties.map((function(t){return r.createElement(r.Fragment,null,r.createElement("tr",null,r.createElement("td",{className:"text-left fw-bolder"}),r.createElement("td",{className:"text-left px-3 width-300"},t.description),r.createElement("td",{className:"text-left px-3"},t.name),r.createElement("td",null,e.referee?r.createElement(m.G,{icon:f.nYk,onClick:function(){return n=t.penaltyId,void l().post("".concat((0,u.AC)(),"/penalty/removeDisqualification?penaltyId=").concat(n),{},{headers:(0,y.Z)()}).then((function(t){v(!0),e.onRemove(),g()}));var n},title:"Wycofaj dyskwalifikacje"}):r.createElement(r.Fragment,null))))})))}}]}),[e.referee]);return r.createElement(r.Fragment,null,r.createElement(i.Z,{columns:p,data:n,pageCount:3,isLoading:s,isFooter:!1,isHeader:!0,cursor:"pointer",manualPagination:!0}))},w=n(5005),S=n(2763),N=n(7182),x=n(5509),k=n(7905),C=n(1881),I=n(6968);function j(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var n=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=n){var r,a,l=[],i=!0,o=!1;try{for(n=n.call(e);!(i=(r=n.next()).done)&&(l.push(r.value),!t||l.length!==t);i=!0);}catch(e){o=!0,a=e}finally{try{i||null==n.return||n.return()}finally{if(o)throw a}}return l}}(e,t)||function(e,t){if(e){if("string"==typeof e)return O(e,t);var n=Object.prototype.toString.call(e).slice(8,-1);return"Object"===n&&e.constructor&&(n=e.constructor.name),"Map"===n||"Set"===n?Array.from(e):"Arguments"===n||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?O(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function O(e,t){(null==t||t>e.length)&&(t=e.length);for(var n=0,r=new Array(t);n<t;n++)r[n]=e[n];return r}var A=function(e){var t=e.show,n=e.handleClose,a=e.eventId,i=e.markedNumbers,o=j((0,r.useState)(!1),2),s=o[0],m=o[1],f=j((0,r.useState)([]),2),y=f[0],v=f[1],g=j((0,r.useState)([]),2),p=g[0],b=g[1];(0,r.useEffect)((function(){t&&((0,N.CE)(n),2===i.length&&(E(),(0,u.TX)(a,i,(function(e){v(e)}))))}),[t]);var E=function(){void 0!==a&&l().get("".concat((0,u.AC)(),"/event/getBasicTeams?eventId=").concat(a)).then((function(e){b(e.data)}))};return(0,r.useEffect)((function(){p.length>0&&y.length>0&&m(!1)}),[p,y]),r.createElement(r.Fragment,null,r.createElement(C.Z,{show:t,onHide:n,backdrop:"static",keyboard:!1,size:"xl"},r.createElement(C.Z.Header,{closeButton:!0,className:"bg-dark-green text-white"},r.createElement(C.Z.Title,{className:"text-white"},"Porównanie wyników")),r.createElement(C.Z.Body,{className:"my-px-3"},s?r.createElement("div",{className:"text-center"},r.createElement(I.Z,{animation:"border",variant:"secondary",size:"lg"})):2!==i.length?r.createElement("h6",{className:"text-center pt-3"},i.length<2?"Zaznacz 2 zawodników klikając na pomarańczowy numer startowy!":"Porównanie możliwe tylko dla 2 zawodników - zaznaczyłeś ".concat(i.length)):r.createElement("div",null,r.createElement("div",{className:"row pt-0 my-px-3 bg-light"},i.map((function(e){var t=p.find((function(t){return t.number===e})),n=null==t?void 0:t.team;return r.createElement("div",{className:"col p-1 ps-4",style:{scale:"1.1"}},n&&r.createElement("div",{className:"d-flex"},r.createElement("div",{className:"align-self-center pe-2"},r.createElement(d.M,{value:e})),r.createElement(c.Ur,{team:n})))}))),r.createElement("div",{className:"row pt-0 my-px-3",style:{backgroundColor:"lightgray"}},i.map((function(e){var t=p.find((function(t){return t.number===e})),n=null==t?void 0:t.team,a=null==t?void 0:t.car;return r.createElement("div",{className:"col p-3 ps-4",style:{scale:"1.1"}},n&&r.createElement(c.He,{line1:((null==a?void 0:a.brand)||"")+" "+((null==a?void 0:a.model)||""),line2:t.carClass.name,carBrand:null==a?void 0:a.brand,driveType:null==a?void 0:a.driveTypeEnum}))}))),r.createElement("h6",{className:"pt-2 text-center"},"Wersja testowa - czasy nie zawierają kar:"),r.createElement("div",{className:"row pt-1"},i.sort((function(e,t){return e-t})).map((function(e,t){var n=0===t,a=n?"col px-0 text-right":"col px-0";return r.createElement(r.Fragment,null,r.createElement("div",{className:a},y.filter((function(t){return t.teamNumber===e})).sort((function(e,t){return e.stageId-t.stageId})).map((function(e,t){var a=t%2==0?"bg-light-gray ":"";return r.createElement("div",{className:""},r.createElement("div",{className:a+"p-1 px-3 d-flex "},n&&r.createElement("div",{style:{marginRight:"auto"}},r.createElement(c.O8,{line1:"PS "+(t+1),line2:"0"})),r.createElement(c.O8,{line1:(0,N.to)(e.score),line2:"0"})))}))))}))))),r.createElement(C.Z.Footer,{className:"justify-content-center"},r.createElement("div",{className:"d-flex"},r.createElement(w.Z,{className:"m-1",variant:"secondary",onClick:n},"Wyjdź")))))},F=n(1251);function P(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function H(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?P(Object(n),!0).forEach((function(t){Z(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):P(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function Z(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function T(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var n=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=n){var r,a,l=[],i=!0,o=!1;try{for(n=n.call(e);!(i=(r=n.next()).done)&&(l.push(r.value),!t||l.length!==t);i=!0);}catch(e){o=!0,a=e}finally{try{i||null==n.return||n.return()}finally{if(o)throw a}}return l}}(e,t)||function(e,t){if(e){if("string"==typeof e)return z(e,t);var n=Object.prototype.toString.call(e).slice(8,-1);return"Object"===n&&e.constructor&&(n=e.constructor.name),"Map"===n||"Set"===n?Array.from(e):"Arguments"===n||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?z(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function z(e,t){(null==t||t>e.length)&&(t=e.length);for(var n=0,r=new Array(t);n<t;n++)r[n]=e[n];return r}var B=function(e){var t,n=T((0,r.useState)(1),2),a=n[0],m=n[1],f=T((0,r.useState)([]),2),y=f[0],v=f[1],g=T((0,r.useState)(!1),2),b=g[0],E=g[1],C=(0,o.TH)(),I=(0,o.TH)().search,j=I.includes("&")?I.indexOf("&"):I.length,O=I.substring(0,j).replace("?",""),P=(0,o.s0)(),Z=O||localStorage.getItem("eventId")||(null===(t=C.state)||void 0===t?void 0:t.eventId),z="GENERALNA",B="GOŚĆ",D=T((0,r.useState)(),2),M=D[0],U=D[1],L=T((0,r.useState)([]),2),R=L[0],G=L[1],K=T((0,r.useState)(!1),2),W=K[0],$=K[1],_=T((0,r.useState)([]),2),q=_[0],Q=_[1],V=T((0,r.useState)([]),2),Y=V[0],X=V[1],J=T((0,r.useState)([]),2),ee=J[0],te=J[1],ne=T((0,r.useState)(z),2),re=ne[0],ae=ne[1],le=T((0,r.useState)(),2),ie=le[0],oe=le[1],ce=T((0,r.useState)(""),2),se=ce[0],ue=ce[1],de=T((0,r.useState)(!0),2),me=(de[0],de[1],T((0,r.useState)(!1),2)),fe=me[0],ye=(me[1],(0,F.pA)(ie,{skip:void 0===ie})),ve=ye.data,ge=void 0===ve?[]:ve,pe=ye.isFetching,be=ye.refetch,Ee=(0,F.bz)({eventId:Z,stageId:ie},{skip:void 0===ie&&void 0===Z}),he=Ee.data,we=void 0===he?[]:he,Se=Ee.isFetching,Ne=Ee.refetch,xe=function(){void 0!==ie&&l().get("".concat((0,u.AC)(),"/event/getEvent?eventId=").concat(Z)).then((function(e){U(H(H({},e.data),{},{date:new Date(e.data.date),signDeadline:new Date(e.data.signDeadline)}))}))};(0,r.useEffect)((function(){void 0===Z&&P("/"),(0,u.Ot)(Z,$)}),[]),(0,r.useEffect)((function(){xe()}),[ie]),(0,r.useEffect)((function(){(0,u.sH)(Z,(function(e){X(e.psOptions||[]),te(e.classesOptions||[])})),xe()}),[]),(0,r.useEffect)((function(){if(!pe&&0!==ge.length){var e=re==="".concat(z,"+").concat(B)?ge:re===z?ge.filter((function(e){return e.className!==B})):ge.filter((function(e){return e.className===re||e.driveType===re}));G((0,N.uG)(e))}}),[ge,re]),(0,r.useEffect)((function(){if(!Se&&0!==we.length){var e=re==="".concat(z,"+").concat(B)?we:re===z?we.filter((function(e){return e.className!==B})):we.filter((function(e){return e.className===re||e.driveType===re}));Q((0,N.uG)(e))}}),[we,re]);var ke=(0,r.useMemo)((function(){return[{width:"5%",id:"place",Header:"P.",accessor:function(e){return e.place},disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(r.Fragment,null," ",e.row.index+1)}},{width:"7%",id:"nr",Header:"Nr",accessor:function(e){return e.number},disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(d.M,{value:e.value,isBold:y.includes(e.value),onClick:function(){return Ce(e.value)}})}},{width:"30%",id:"team",Header:"Załoga",disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(c.Ur,{team:e.row.original})}},{width:"30%",id:"car",Header:"Samochód",disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(c.He,{line1:e.row.original.car,line2:e.row.original.className,carBrand:e.row.original.brand,driveType:e.row.original.driveType})}},{width:"15%",id:"score",Header:"Czas / kary",accessor:function(e){return e.stageScore},disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(c.O8,{line1:e.row.original.stageScore,line2:e.row.original.totalPenalty})}},{width:"15%",id:"result",Header:"Wynik / straty",accessor:function(e){return e.stageScore},disableFilters:!0,disableSortBy:!0,Cell:function(e){return r.createElement(c.AK,{line1:e.row.original.totalTime,line2:e.row.original.timeTo,line3:e.row.original.timeToFirst})}}]}),[]),Ce=function(e){y.includes(e)?y.splice(y.indexOf(e),1):y.push(e),v(Array.from(y))},Ie=function(e){if(y.includes(e.values.nr))return"yellow"};return r.createElement(r.Fragment,null,r.createElement("div",{className:"row card-body mx-0"},r.createElement("h4",null,(null==M?void 0:M.name)||""),r.createElement("div",{className:"col-xl-8 d-flex justify-content-center"},((null==M?void 0:M.logoPathFile)||(null==M?void 0:M.logoPath))&&r.createElement("div",{className:"col-6 align-self-center"},r.createElement("div",{className:"m-0 text-center"},M.logoPathFile?r.createElement("img",{id:"eventImage"+M.eventId,style:{maxHeight:"150px"},className:"img-fluid rounded float-left",src:"data:image/jpg;base64,"+M.logoPathFile,alt:"Logo"}):r.createElement("img",{style:{maxHeight:"150px"},className:"img-fluid rounded float-left",src:M.logoPath,alt:"Logo"}))),r.createElement("div",{className:"col-6"},r.createElement(s.Q,{label:"Klasyfikacja",options:ee,handleChange:function(e){ae(ee.find((function(t){return t.value===e})).label)},isValid:!0}),r.createElement(s.Q,{label:"PS",options:Y,value:sessionStorage.getItem("scoreStageId"),handleChange:function(e){oe(e),ue(Y.find((function(t){return t.value===e})).label),sessionStorage.setItem("scoreStageId",e)},isValid:!0}))),r.createElement("div",{className:"col-xl-4"},r.createElement("div",{className:"m-2 text-center"},W&&r.createElement(r.Fragment,null,r.createElement(w.Z,{className:"m-1",variant:"success",onClick:function(){return P("/add_score",{state:{eventId:Z}})}},"Dodaj wynik"),r.createElement(w.Z,{className:"m-1",variant:"primary",onClick:function(){return P("/add_penalty",{state:{eventId:Z}})}},"Dodaj kare"),r.createElement(S.O,{variant:"secondary",isLoading:fe,onClick:function(){window.open("".concat((0,u.AC)(),"/file/getScoresFile?eventId=").concat(Z),"_blank")},msg:"Generuj plik z zestawieniem wyników",loadingMsg:"Trwa generowanie\r pliku z wynikami"})),r.createElement(w.Z,{className:"m-1",variant:"primary",onClick:function(){Ne(),be()}},"Odśwież"),r.createElement(w.Z,{className:"m-1",variant:"success",onClick:function(){return E(!0)}},"Porównaj wyniki")))),r.createElement("div",{className:"row pt-0 mx-0 card-body"},r.createElement("div",{className:"fw-bold alert alert-secondary p-1 mb-2",role:"alert"},"".concat(se)),r.createElement(x.Z,{activeKey:a,onSelect:function(e){return m(e)},className:"mb-1 fw-bold text-dark device-small"},r.createElement(k.Z,{eventKey:1,title:"Czas NA",className:"alert-secondary"},r.createElement("div",{className:"my-pe-1"},r.createElement("div",{className:"shadow bg-body rounded"},r.createElement(i.Z,{columns:ke,data:R,pageCount:3,isLoading:pe,isFooter:!1,isHeader:!0,cursor:"pointer",manualPagination:!0,highlightRow:function(e){return Ie(e)}})))),r.createElement(k.Z,{eventKey:2,title:"Suma PO"},r.createElement("div",{className:"my-ps-1"},r.createElement("div",{className:"shadow bg-body rounded"},r.createElement(i.Z,{columns:ke,data:q,pageCount:3,isLoading:Se,isFooter:!1,isHeader:!0,cursor:"pointer",manualPagination:!0,highlightRow:function(e){return Ie(e)}}))))),r.createElement("div",{className:"col-xl-12 px-1"},r.createElement("div",{className:"shadow bg-body rounded mt-4 p-0"},r.createElement("div",{className:"fw-bold alert alert-secondary p-1 m-0",role:"alert"},"Kary"),r.createElement(p,{eventId:Z,onRemove:xe,referee:W})),r.createElement("div",{className:"shadow bg-body rounded mt-4"},r.createElement("div",{className:"fw-bold alert alert-secondary p-1 m-0",role:"alert"},"Dyskwalifikacje / Wycofania"),r.createElement(h,{eventId:Z,onRemove:xe,referee:W})))),b&&r.createElement(A,{show:!0,handleClose:function(){return E()},eventId:Z,markedNumbers:y}))}}}]);
//# sourceMappingURL=main.20983b71530f092d7096.js.map