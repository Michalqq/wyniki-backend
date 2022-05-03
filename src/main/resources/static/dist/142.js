"use strict";(self.webpackChunkjavascript=self.webpackChunkjavascript||[]).push([[142],{2763:function(e,t,n){n.d(t,{E:function(){return c},O:function(){return o}});var a=n(7294),r=n(5005),l=n(6968),o=function(e){var t=e.variant,n=e.onClick,o=e.isLoading,c=e.msg,i=e.loadingMsg;return a.createElement(r.Z,{className:"m-1",variant:t,onClick:n,disabled:o},o?a.createElement(a.Fragment,null,a.createElement(l.Z,{size:"sm",animation:"border"})," "," "+i):a.createElement(a.Fragment,null,c))},c=function(e){var t=e.onClick,n=e.label,r=e.isConfirmed,l=void 0!==r&&r,o=e.name,c=e.value;return a.createElement("div",{className:"form-check"},a.createElement("input",{onClick:t,defaultChecked:l,className:"form-check-input",type:"radio",name:o,value:c}),a.createElement("label",{className:"form-check-label"},n))}},9661:function(e,t,n){n.d(t,{AK:function(){return l},He:function(){return r},O8:function(){return o},Ur:function(){return a}});var a=function(e){var t=e.team,n=function(e){return null!=e?" ("+e+")":""};return React.createElement("div",{className:"float-left"},React.createElement("div",{className:"d-flex pt-1",style:{flexWrap:"wrap"}},React.createElement("h6",{className:"text-left font14 fw-bolder fst-italic m-0"},t.driver),React.createElement("p",{className:"text-left font12 p-0 div-club align-self-center"},n(t.club))),React.createElement("div",{className:"d-flex",style:{flexWrap:"wrap"}},React.createElement("p",{className:"text-left font13 fw-bolder m-0 p-0"},t.coDriver),React.createElement("p",{className:"text-left font12 p-0 div-club align-self-center"},n(t.coClub))),React.createElement("p",{className:"text-left font13 fw-bolder m-0 p-0"},t.teamName||""))},r=function(e){var t=e.line1,n=e.line2,a=e.carBrand,r=e.driveType,l=null==a?void 0:a.toLowerCase().replace(/ /g,"");"vw"===l&&(l="volkswagen");var o="https://vehapi.com/img/car-logos/".concat(l,".png");return React.createElement("div",{className:"col-12 d-flex"},React.createElement("div",{className:"col-xl-3 col-4 py-2 align-self-center"},React.createElement("img",{className:"img-fluid",style:{height:"23px"},src:o,alt:""})),React.createElement("div",{className:"col-xl-9 col-7"},React.createElement("h6",{className:"font13 fw-bolder m-0"},t),r&&React.createElement("p",{className:"font12 m-0 p-0"},r),React.createElement("p",{className:"font12 m-0 p-0 fw-bolder"},n)))},l=function(e){var t=e.line1,n=e.line2,a=e.line3;return React.createElement("div",{className:"float-left"},React.createElement("h6",{className:"font13 fw-bolder m-0"},t),React.createElement("p",{className:"font12 m-0 p-0"},n),React.createElement("p",{className:"font12 m-0 p-0"},a))},o=function(e){var t=e.line1,n=e.line2;return React.createElement("div",{className:"float-left"},React.createElement("h6",{className:"font13 fw-bolder m-0"},t),"0"===n?React.createElement(React.Fragment,null):React.createElement("p",{className:"font11 m-0 p-0 fw-bolder text-danger"},"+"+n+" s"))}},3992:function(e,t,n){n.d(t,{M:function(){return l}});var a=n(7294),r=n(7977),l=function(e){var t=e.value;return a.createElement(r.Z,{className:"number-badge",bg:"",style:{paddingTop:"5px",justifyContent:"center",display:"grid",width:"22px",height:"22px",borderRadius:"20px",fontSize:"11px"}},t)}},496:function(e,t,n){n.d(t,{Z:function(){return h}});var a=n(7294),r=n(9521),l=n(6157),o=function(){return a.createElement(a.Fragment,null,a.createElement("div",{id:"table-loader",className:"flex justify-content-center align-items-center flex-col-reverse u-padding-top-xxl u-padding-xxl"},a.createElement("div",{className:"loader-text"},"Pobieranie danych ..."),a.createElement("div",{className:"loader-container"},a.createElement("div",null),a.createElement("div",null),a.createElement("div",null))))};function c(){return c=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&(e[a]=n[a])}return e},c.apply(this,arguments)}var i=function(e){var t=e.isHeader,n=e.headerGroups;return t?a.createElement("thead",{className:"thead-dark"},n.map((function(e,t){return a.createElement("tr",c({key:"tableHeader"+t,style:{display:"table-row"}},e.getHeaderGroupProps(),{className:"l-table-row text-white bg-dark"}),e.headers.map((function(e){return a.createElement("th",c({key:"tableHeaderCell"+e.id},e.getHeaderProps(),{className:"px-1 py-0 align-middle",style:{width:e.width,wordBreak:"break-word"}}),a.createElement("div",{className:"flex flex-column"},a.createElement("div",c({},e.getHeaderProps(e.getSortByToggleProps()),{className:"py-1 my-0 flex align-items-end justify-content-center h6 l-row g-brand2-c table-column-header"}),a.createElement("span",{className:"font13 flex"},null!=e.Header&&e.render("Header")),a.createElement("span",{className:"font13 g-gray4-c"},function(e){return e.isSorted?e.isSortedDesc?" ▼":" ▲":""}(e))),a.createElement("div",{className:"flex align-items-end"},e.canFilter?e.render("Filter"):null)))})))}))):a.createElement(a.Fragment,null)};function s(){return s=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&(e[a]=n[a])}return e},s.apply(this,arguments)}var u=function(e){var t=e.isFooter,n=e.footerGroups;return t?a.createElement("tfoot",null,n.map((function(e,t){return a.createElement("tr",s({},e.getFooterGroupProps(),{key:"footerRow"+t}),e.headers.map((function(e){return a.createElement("td",s({},e.getFooterProps(),{key:"footerRowCell"+e.id,className:"u-padding-top-m knor-table-sum-result"}),e.render("Footer"))})))}))):a.createElement(a.Fragment,null)};function m(){return m=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&(e[a]=n[a])}return e},m.apply(this,arguments)}var d=function(e){var t=e.headerGroups;return a.createElement("tbody",null,a.createElement("tr",{style:{textDecoration:"none",display:"table-row"},className:"font14 l-table-row u-border u-link"},t[0].headers.map((function(e){return a.createElement("th",m({key:"emptyRowCell"+e.id},e.getHeaderProps(),{className:"l-col u-padding-m u-padding-s u-padding-top-m u-border",style:{width:e.width}}),t[0].headers[0].id===e.id?"Brak danych":"___")}))))};function f(){return f=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var a in n)Object.prototype.hasOwnProperty.call(n,a)&&(e[a]=n[a])}return e},f.apply(this,arguments)}function p(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var n=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=n){var a,r,l=[],o=!0,c=!1;try{for(n=n.call(e);!(o=(a=n.next()).done)&&(l.push(a.value),!t||l.length!==t);o=!0);}catch(e){c=!0,r=e}finally{try{o||null==n.return||n.return()}finally{if(c)throw r}}return l}}(e,t)||function(e,t){if(e){if("string"==typeof e)return g(e,t);var n=Object.prototype.toString.call(e).slice(8,-1);return"Object"===n&&e.constructor&&(n=e.constructor.name),"Map"===n||"Set"===n?Array.from(e):"Arguments"===n||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?g(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function g(e,t){(null==t||t>e.length)&&(t=e.length);for(var n=0,a=new Array(t);n<t;n++)a[n]=e[n];return a}var v=function(e){var t=e.column,n=t.filterValue,r=t.setFilter;return a.createElement("input",{value:n||"",onChange:function(e){r(e.target.value||void 0)},placeholder:""})};function h(e){var t=e.columns,n=e.data,c=e.renderRowSubComponent,s=e.updateMyData,m=e.isLoading,g=e.isFooter,h=e.isHeader,E=void 0===h||h,b=e.pageCount,y=(e.expandOn,e.manualPagination),w=void 0!==y&&y,x=e.manualFilters,N=void 0!==x&&x,R=e.hiddenColumns,k=void 0===R?[]:R,C=e.sortBy,F=void 0===C?[]:C,P=e.cursor,O=void 0===P?"text":P,j=e.fetchData,S=void 0===j?function(){}:j,H=e.onPageIndexChange,G=void 0===H?function(){}:H,L=e.onFilterChange,A=void 0===L?function(){}:L,B=e.onRowClick,D=void 0===B?function(){}:B,T=e.highlightRow,I=void 0===T?function(){return""}:T,M=e.textDecoration,U=void 0===M?"none":M,Z=e.fontWeight,W=void 0===Z?"350":Z,_=p((0,a.useState)(0),2),z=(_[0],_[1],a.useMemo((function(){return{text:function(e,t,n){return e.filter((function(e){var a=e.values[t];return void 0===a||String(a).toLowerCase().startsWith(String(n).toLowerCase())}))}}}),[])),K=a.useMemo((function(){return{Filter:v}}),[]),V=(0,r.useTable)({columns:t,data:n,defaultColumn:K,filterTypes:z,initialState:{pageIndex:0,hiddenColumns:k,sortBy:F},updateMyData:s,autoResetPage:!1,autoResetFilters:!1,isLoading:m,isFooter:g,isHeader:E,manualPagination:w,pageCount:b,manualFilters:N},r.useFilters,r.useGlobalFilter,r.useSortBy,r.useExpanded,r.usePagination),$=V.getTableProps,q=V.getTableBodyProps,J=V.headerGroups,Q=V.prepareRow,X=V.page,Y=(V.canPreviousPage,V.canNextPage,V.pageOptions,V.pageCount,V.gotoPage),ee=(V.nextPage,V.previousPage,V.visibleColumns),te=V.footerGroups,ne=V.state,ae=ne.pageIndex,re=ne.filters;(0,a.useEffect)((function(){Y(0)}),[re]);var le=p((0,l.Z)(re,900),1)[0];return(0,a.useEffect)((function(){le&&(Y(0),A(le))}),[le]),(0,a.useEffect)((function(){G(ae)}),[ae]),(0,a.useEffect)((function(){le&&S({pageIndex:ae,filters:le})}),[ae,le]),m?a.createElement(a.Fragment,null,a.createElement("table",f({id:"traffic-simulator-table"},$()),a.createElement(i,{isHeader:E,headerGroups:J})),a.createElement(o,{isLoading:m})):n.length?a.createElement(a.Fragment,null,a.createElement("table",f({className:"table table-striped",id:"traffic-simulator-table"},$()),a.createElement(i,{isHeader:E,headerGroups:J}),a.createElement("tbody",q(),X.map((function(e,t){return Q(e),a.createElement(a.Fragment,{key:"row"+t},a.createElement("tr",f({},e.getRowProps({style:{cursor:O,textDecoration:U,fontWeight:W,display:"table-row",background:I(e)}}),{className:"align-middle"}),e.cells.map((function(t,n){return a.createElement("td",f({key:n,style:{maxWidth:"0px",width:"unset"}},t.getCellProps({onClick:t.column.disableRowClick?function(){}:function(){return D(e,t)}}),{className:"p-0 m-0"}),a.createElement("div",{title:t.value,className:"truncate-text truncate-inline nkor-text-align-right"},t.render("Cell")))}))),(e.isExpanded||"true"===e.values.expand)&&a.createElement("tr",null,a.createElement("td",{colSpan:ee.length},c({row:e}))))}))),a.createElement(u,{isFooter:g,footerGroups:te}))):a.createElement(a.Fragment,null,a.createElement("table",f({id:"traffic-simulator-table"},$()),a.createElement(i,{isHeader:E,headerGroups:J}),a.createElement(d,{headerGroups:J})))}},4010:function(e,t,n){n.d(t,{L:function(){return r},Z:function(){return l}});var a=n(9952);function r(e,t,n){fetch(e,{headers:(0,a.Z)()}).then((function(e){return e.blob().then((function(e){var a=document.createElement("a");a.href=URL.createObjectURL(e),a.setAttribute("download",t),a.click(),n&&n()}))}))}function l(e,t){var n=function(e){for(var t=window.atob(e),n=t.length,a=new Uint8Array(n),r=0;r<n;r++)a[r]=t.charCodeAt(r);return a.buffer}(e),a=new Blob([n],{type:"application/"+t.split(".").pop()}),r=document.createElement("a");r.href=URL.createObjectURL(a),r.setAttribute("download",t),r.click()}}}]);
//# sourceMappingURL=142.js.map