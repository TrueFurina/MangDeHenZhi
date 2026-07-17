/*! For license information please see UePage.umd.min.442.js.LICENSE.txt */
"use strict";(("undefined"!=typeof self?self:this).webpackChunkUePage=("undefined"!=typeof self?self:this).webpackChunkUePage||[]).push([[442],{2719:(t,e,i)=>{i.d(e,{a:()=>r});var o=Object.defineProperty,n=Object.getOwnPropertyDescriptor,r=(t,e,i,r)=>{for(var s,a=r>1?void 0:r?n(e,i):e,l=t.length-1;l>=0;l--)(s=t[l])&&(a=(r?s(e,i,a):s(a))||a);return r&&a&&o(e,i,a),a}},3084:(t,e,i)=>{i.d(e,{a:()=>h,b:()=>d});var o=Object.create,n=Object.defineProperty,r=Object.getOwnPropertyDescriptor,s=Object.getOwnPropertyNames,a=Object.getPrototypeOf,l=Object.prototype.hasOwnProperty,h=(t,e)=>()=>(e||t((e={exports:{}}).exports,e),e.exports),d=(t,e,i)=>(i=null!=t?o(a(t)):{},((t,e,i,o)=>{if(e&&"object"==typeof e||"function"==typeof e)for(let a of s(e))!l.call(t,a)&&a!==i&&n(t,a,{get:()=>e[a],enumerable:!(o=r(e,a))||o.enumerable});return t})(!e&&t&&t.__esModule?i:n(i,"default",{value:t,enumerable:!0}),t))},4356:(t,e,i)=>{i.d(e,{a:()=>ne,b:()=>re,c:()=>se,d:()=>le,e:()=>Oe,f:()=>Pe,g:()=>Ie,h:()=>Me,i:()=>Ee,j:()=>Te});var o=i(9061),n=(i(9375),i(7534),i(2482),i(7067),i(4147),i(2438),i(2989),i(2266),i(9819),i(7327),i(20),i(4786),i(4840),i(9033),i(8903),i(1018),i(1415),i(4448),i(8871),i(6539),i(7404),i(5803),i(3912),i(7182),i(4434),i(1412),i(1883),i(7905),{}),r=Uint8Array,s=Uint16Array,a=Int32Array,l=new r([0,0,0,0,0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,0,0,0,0]),h=new r([0,0,0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11,12,12,13,13,0,0]),d=new r([16,17,18,0,8,7,9,6,10,5,11,4,12,3,13,2,14,1,15]),u=function(t,e){for(var i=new s(31),o=0;o<31;++o)i[o]=e+=1<<t[o-1];var n=new a(i[30]);for(o=1;o<30;++o)for(var r=i[o];r<i[o+1];++r)n[r]=r-i[o]<<5|o;return{b:i,r:n}},p=u(l,2),c=p.b,f=p.r;c[28]=258,f[258]=28;var m=u(h,0).b,v=new s(32768);for(w=0;w<32768;++w)_=(61680&(_=(52428&(_=(43690&w)>>1|(21845&w)<<1))>>2|(13107&_)<<2))>>4|(3855&_)<<4,v[w]=((65280&_)>>8|(255&_)<<8)>>1;var _,y=function(t,e,i){for(var o=t.length,n=0,r=new s(e);n<o;++n)t[n]&&++r[t[n]-1];var a,l=new s(e);for(n=1;n<e;++n)l[n]=l[n-1]+r[n-1]<<1;if(i){a=new s(1<<e);var h=15-e;for(n=0;n<o;++n)if(t[n])for(var d=n<<4|t[n],u=e-t[n],p=l[t[n]-1]++<<u,c=p|(1<<u)-1;p<=c;++p)a[v[p]>>h]=d}else for(a=new s(o),n=0;n<o;++n)t[n]&&(a[n]=v[l[t[n]-1]++]>>15-t[n]);return a},g=new r(288);for(w=0;w<144;++w)g[w]=8;for(w=144;w<256;++w)g[w]=9;for(w=256;w<280;++w)g[w]=7;for(w=280;w<288;++w)g[w]=8;var b=new r(32);for(w=0;w<32;++w)b[w]=5;var w,A=y(g,9,1),C=y(b,5,1),L=function(t){for(var e=t[0],i=1;i<t.length;++i)t[i]>e&&(e=t[i]);return e},S=function(t,e,i){var o=e/8|0;return(t[o]|t[o+1]<<8)>>(7&e)&i},k=function(t,e){var i=e/8|0;return(t[i]|t[i+1]<<8|t[i+2]<<16)>>(7&e)},x=function(t){return(t+7)/8|0},$=function(t,e,i){(null==e||e<0)&&(e=0),(null==i||i>t.length)&&(i=t.length);var o=new r(i-e);return o.set(t.subarray(e,i)),o},O=["unexpected EOF","invalid block type","invalid length/literal","invalid distance","stream finished","no stream handler",,"no callback","invalid UTF-8 data","extra field too long","date not in range 1980-2099","filename too long","stream finishing","invalid zip data"],P=function(t,e,i){var o=new Error(e||O[t]);if(o.code=t,Error.captureStackTrace&&Error.captureStackTrace(o,P),!i)throw o;return o},I=function(t,e,i,o){var n=t.length,s=o?o.length:0;if(!n||e.f&&!e.l)return i||new r(0);var a=!i||2!=e.i,u=e.i;i||(i=new r(3*n));var p=function(t){var e=i.length;if(t>e){var o=new r(Math.max(2*e,t));o.set(i),i=o}},f=e.f||0,v=e.p||0,_=e.b||0,g=e.l,b=e.d,w=e.m,O=e.n,I=8*n;do{if(!g){f=S(t,v,1);var M=S(t,v+1,3);if(v+=3,!M){var E=t[(q=x(v)+4)-4]|t[q-3]<<8,T=q+E;if(T>n){u&&P(0);break}a&&p(_+E),i.set(t.subarray(q,T),_),e.b=_+=E,e.p=v=8*T,e.f=f;continue}if(1==M)g=A,b=C,w=9,O=5;else if(2==M){var U=S(t,v,31)+257,j=S(t,v+10,15)+4,N=U+S(t,v+5,31)+1;v+=14;for(var z=new r(N),D=new r(19),F=0;F<j;++F)D[d[F]]=S(t,v+3*F,7);v+=3*j;var V=L(D),B=(1<<V)-1,H=y(D,V,1);for(F=0;F<N;){var q,R=H[S(t,v,B)];if(v+=15&R,(q=R>>4)<16)z[F++]=q;else{var Z=0,J=0;for(16==q?(J=3+S(t,v,3),v+=2,Z=z[F-1]):17==q?(J=3+S(t,v,7),v+=3):18==q&&(J=11+S(t,v,127),v+=7);J--;)z[F++]=Z}}var W=z.subarray(0,U),K=z.subarray(U);w=L(W),O=L(K),g=y(W,w,1),b=y(K,O,1)}else P(1);if(v>I){u&&P(0);break}}a&&p(_+131072);for(var G=(1<<w)-1,X=(1<<O)-1,Q=v;;Q=v){var Y=(Z=g[k(t,v)&G])>>4;if((v+=15&Z)>I){u&&P(0);break}if(Z||P(2),Y<256)i[_++]=Y;else{if(256==Y){Q=v,g=null;break}var tt=Y-254;if(Y>264){var et=l[F=Y-257];tt=S(t,v,(1<<et)-1)+c[F],v+=et}var it=b[k(t,v)&X],ot=it>>4;it||P(3),v+=15&it;K=m[ot];if(ot>3){et=h[ot];K+=k(t,v)&(1<<et)-1,v+=et}if(v>I){u&&P(0);break}a&&p(_+131072);var nt=_+tt;if(_<K){var rt=s-K,st=Math.min(K,nt);for(rt+_<0&&P(3);_<st;++_)i[_]=o[rt+_]}for(;_<nt;_+=4)i[_]=i[_-K],i[_+1]=i[_+1-K],i[_+2]=i[_+2-K],i[_+3]=i[_+3-K];_=nt}}e.l=g,e.p=Q,e.b=_,e.f=f,g&&(f=1,e.m=w,e.d=b,e.n=O)}while(!f);return _==i.length?i:$(i,0,_)},M=new r(0),E=function(t,e,i){for(var o=t(),n=t.toString(),r=n.slice(n.indexOf("[")+1,n.lastIndexOf("]")).replace(/\s+/g,"").split(","),s=0;s<o.length;++s){var a=o[s],l=r[s];if("function"==typeof a){e+=";"+l+"=";var h=a.toString();if(a.prototype)if(-1!=h.indexOf("[native code]")){var d=h.indexOf(" ",8)+1;e+=h.slice(d,h.indexOf("(",d))}else for(var u in e+=h,a.prototype)e+=";"+l+".prototype."+u+"="+a.prototype[u].toString();else e+=h}else i[l]=a}return e},T=[],U=function(t,e,i,o){if(!T[i]){for(var r="",s={},a=t.length-1,l=0;l<a;++l)r=E(t[l],r,s);T[i]={c:E(t[a],r,s),e:s}}var h=function(t,e){var i={};for(var o in t)i[o]=t[o];for(var o in e)i[o]=e[o];return i}({},T[i].e);return function(t,e,i,o,r){var s=new Worker(n[e]||(n[e]=URL.createObjectURL(new Blob([t+';addEventListener("error",function(e){e=e.error;postMessage({$e$:[e.message,e.code,e.stack]})})'],{type:"text/javascript"}))));return s.onmessage=function(t){var e=t.data,i=e.$e$;if(i){var o=new Error(i[0]);o.code=i[1],o.stack=i[2],r(o,null)}else r(null,e)},s.postMessage(i,o),s}(T[i].c+";onmessage=function(e){for(var k in e.data)self[k]=e.data[k];onmessage="+e.toString()+"}",i,h,function(t){var e=[];for(var i in t)t[i].buffer&&e.push((t[i]=new t[i].constructor(t[i])).buffer);return e}(h),o)},j=function(){return[r,s,a,l,h,d,c,m,A,C,v,O,y,L,S,k,x,$,P,I,H,N,z]},N=function(t){return postMessage(t,[t.buffer])},z=function(t){return t&&{out:t.size&&new r(t.size),dictionary:t.dictionary}},D=function(t,e){return t[e]|t[e+1]<<8},F=function(t,e){return(t[e]|t[e+1]<<8|t[e+2]<<16|t[e+3]<<24)>>>0},V=function(t,e){return F(t,e)+4294967296*F(t,e+4)};function B(t,e,i){return i||(i=e,e={}),"function"!=typeof i&&P(7),function(t,e,i,o,n,r){var s=U(i,o,n,(function(t,e){s.terminate(),r(t,e)}));return s.postMessage([t,e],e.consume?[t.buffer]:[]),function(){s.terminate()}}(t,e,[j],(function(t){return N(H(t.data[0],z(t.data[1])))}),1,i)}function H(t,e){return I(t,{i:2},e&&e.out,e&&e.dictionary)}var q=typeof TextDecoder<"u"&&new TextDecoder;try{q.decode(M,{stream:!0})}catch{}function R(t,e){if(e){for(var i="",o=0;o<t.length;o+=16384)i+=String.fromCharCode.apply(null,t.subarray(o,o+16384));return i}if(q)return q.decode(t);var n=function(t){for(var e="",i=0;;){var o=t[i++],n=(o>127)+(o>223)+(o>239);if(i+n>t.length)return{s:e,r:$(t,i-1)};n?3==n?(o=((15&o)<<18|(63&t[i++])<<12|(63&t[i++])<<6|63&t[i++])-65536,e+=String.fromCharCode(55296|o>>10,56320|1023&o)):e+=1&n?String.fromCharCode((31&o)<<6|63&t[i++]):String.fromCharCode((15&o)<<12|(63&t[i++])<<6|63&t[i++]):e+=String.fromCharCode(o)}}(t),r=n.s;return(i=n.r).length&&P(8),r}var Z=function(t,e){for(;1!=D(t,e);e+=4+D(t,e+2));return[V(t,e+12),V(t,e+4),V(t,e+20)]},J="function"==typeof queueMicrotask?queueMicrotask:"function"==typeof setTimeout?setTimeout:function(t){t()};function W(t,e,i){i||(i=e,e={}),"function"!=typeof i&&P(7);var o=[],n=function(){for(var t=0;t<o.length;++t)o[t]()},s={},a=function(t,e){J((function(){i(t,e)}))};J((function(){a=i}));for(var l=t.length-22;101010256!=F(t,l);--l)if(!l||t.length-l>65558)return a(P(13,0,1),null),n;var h=D(t,l+8);if(h){var d=h,u=F(t,l+16),p=4294967295==u||65535==d;if(p){var c=F(t,l-12);(p=101075792==F(t,c))&&(d=h=F(t,c+32),u=F(t,c+48))}for(var f=e&&e.filter,m=function(e){var i=function(t,e,i){var o=D(t,e+28),n=R(t.subarray(e+46,e+46+o),!(2048&D(t,e+8))),r=e+46+o,s=F(t,e+20),a=i&&4294967295==s?Z(t,r):[s,F(t,e+24),F(t,e+42)],l=a[0],h=a[1],d=a[2];return[D(t,e+10),l,h,n,r+D(t,e+30)+D(t,e+32),d]}(t,u,p),l=i[0],d=i[1],c=i[2],m=i[3],v=i[4],_=i[5],y=function(t,e){return e+30+D(t,e+26)+D(t,e+28)}(t,_);u=v;var g=function(t,e){t?(n(),a(t,null)):(e&&(s[m]=e),--h||a(null,s))};if(!f||f({name:m,size:d,originalSize:c,compression:l}))if(l)if(8==l){var b=t.subarray(y,y+d);if(d<32e4)try{g(null,H(b,{out:new r(c)}))}catch(t){g(t,null)}else o.push(B(b,{size:c},g))}else g(P(14,"unknown compression type "+l,1),null);else g(null,$(t,y,y+d));else g(null,null)},v=0;v<d;++v)m()}else a(null,{});return n}function K(t){return(Array.isArray(t)?t:t.issues).reduce(((t,e)=>{if(e.path){let i=e.path.map((({key:t})=>t)).join(".");t.nested[i]=[...t.nested[i]||[],e.message]}else t.root=[...t.root||[],e.message];return t}),{nested:{}})}var G=class extends Error{constructor(t){super(t[0].message),(0,o.A)(this,"issues",void 0),this.name="ValiError",this.issues=t}};function X(t,e){return{reason:t?.reason,validation:e.validation,origin:t?.origin||"value",message:e.message,input:e.input,abortEarly:t?.abortEarly,abortPipeEarly:t?.abortPipeEarly}}function Q(t,e){return{reason:e,origin:t?.origin,abortEarly:t?.abortEarly,abortPipeEarly:t?.abortPipeEarly}}function Y(t,e,i,o){if(!e||!e.length)return{output:t};let n,r,s=t;for(let t of e){let e=t(s);if(e.issue){n=n||Q(i,o);let t=X(n,e.issue);if(r?r.push(t):r=[t],n.abortEarly||n.abortPipeEarly)break}else s=e.output}return r?{issues:r}:{output:s}}function tt(t,e){return t&&"string"!=typeof t?[void 0,t]:[t,e]}function et(t,e,i,o,n,r){return{issues:[{reason:e,validation:i,origin:t?.origin||"value",message:o,input:n,issues:r,abortEarly:t?.abortEarly,abortPipeEarly:t?.abortPipeEarly}]}}function it(t,e,i){let[o,n]=tt(e,i);return{schema:"array",array:{item:t},async:!1,_parse(e,i){if(!Array.isArray(e))return et(i,"type","array",o||"Invalid type",e);let r,s=[];for(let o=0;o<e.length;o++){let n=e[o],a=t._parse(n,i);if(a.issues){let t={schema:"array",input:e,key:o,value:n};for(let e of a.issues)e.path?e.path.unshift(t):e.path=[t],r?.push(e);if(r||(r=a.issues),i?.abortEarly)break}else s.push(a.output)}return r?{issues:r}:Y(s,n,i,"array")}}}function ot(t,e){let[i,o]=tt(t,e);return{schema:"boolean",async:!1,_parse:(t,e)=>"boolean"!=typeof t?et(e,"type","boolean",i||"Invalid type",t):Y(t,o,e,"boolean")}}function nt(t,e){return{schema:"literal",literal:t,async:!1,_parse:(i,o)=>i!==t?et(o,"type","literal",e||"Invalid type",i):{output:i}}}function rt(t,e){let[i,o]=tt(t,e);return{schema:"number",async:!1,_parse:(t,e)=>"number"!=typeof t?et(e,"type","number",i||"Invalid type",t):Y(t,o,e,"number")}}function st(t,e,i){let o,[n,r]=tt(e,i);return{schema:"object",object:t,async:!1,_parse(e,i){if(!e||"object"!=typeof e)return et(i,"type","object",n||"Invalid type",e);o=o||Object.entries(t);let s,a={};for(let[t,n]of o){let o=e[t],r=n._parse(o,i);if(r.issues){let n={schema:"object",input:e,key:t,value:o};for(let t of r.issues)t.path?t.path.unshift(n):t.path=[n],s?.push(t);if(s||(s=r.issues),i?.abortEarly)break}else a[t]=r.output}return s?{issues:s}:Y(a,r,i,"object")}}}function at(t){return{schema:"optional",wrapped:t,async:!1,_parse:(e,i)=>void 0===e?{output:e}:t._parse(e,i)}}function lt(t,e){let[i,o]=tt(t,e);return{schema:"string",async:!1,_parse:(t,e)=>"string"!=typeof t?et(e,"type","string",i||"Invalid type",t):Y(t,o,e,"string")}}var ht=["__proto__","prototype","constructor"];function dt(t,e,i,o){let[n,r,s]=function(t,e,i){if("object"==typeof t&&!Array.isArray(t)){let[o,n]=tt(e,i);return[t,o,n]}let[o,n]=tt(t,e);return[void 0,o,n]}(e,i,o);return{schema:"tuple",tuple:{items:t,rest:n},async:!1,_parse(e,i){if(!Array.isArray(e)||!n&&t.length!==e.length||n&&t.length>e.length)return et(i,"type","tuple",r||"Invalid type",e);let o,a=[];for(let n=0;n<t.length;n++){let r=e[n],s=t[n]._parse(r,i);if(s.issues){let t={schema:"tuple",input:e,key:n,value:r};for(let e of s.issues)e.path?e.path.unshift(t):e.path=[t],o?.push(e);if(o||(o=s.issues),i?.abortEarly)break}else a[n]=s.output}if(n)for(let r=t.length;r<e.length;r++){let t=e[r],s=n._parse(t,i);if(s.issues){let n={schema:"tuple",input:e,key:r,value:t};for(let t of s.issues)t.path?t.path.unshift(n):t.path=[n],o?.push(t);if(o||(o=s.issues),i?.abortEarly)break}else a[r]=s.output}return o?{issues:o}:Y(a,s,i,"tuple")}}}function ut(t,e){return{schema:"union",union:t,async:!1,_parse(i,o){let n,r;for(let e of t){let t=e._parse(i,o);if(!t.issues){r=[t.output];break}if(n)for(let e of t.issues)n.push(e);else n=t.issues}return r?{output:r[0]}:et(o,"type","union",e||"Invalid type",i,n)}}}function pt(t,e,i){let[o,n]=tt(e,i);return st(t.reduce(((t,e)=>({...t,...e.object})),{}),o,n)}function ct(t,e){return i=>i>t?{issue:{validation:"max_value",message:e||"Invalid value",input:i}}:{output:i}}function ft(t,e){return i=>i<t?{issue:{validation:"min_value",message:e||"Invalid value",input:i}}:{output:i}}var mt,vt=Object.create,_t=Object.defineProperty,yt=Object.getOwnPropertyDescriptor,gt=Object.getOwnPropertyNames,bt=Object.getPrototypeOf,wt=Object.prototype.hasOwnProperty,At=(t,e)=>function(){return e||(0,t[gt(t)[0]])((e={exports:{}}).exports,e),e.exports},Ct=(t,e,i)=>(((t,e,i)=>{e in t?_t(t,e,{enumerable:!0,configurable:!0,writable:!0,value:i}):t[e]=i})(t,"symbol"!=typeof e?e+"":e,i),i),Lt=At({"../../node_modules/.pnpm/@rgba-image+copy@0.1.3/node_modules/@rgba-image/copy/dist/index.js"(t){Object.defineProperty(t,"__esModule",{value:!0}),t.copy=void 0;t.copy=(t,e,i=0,o=0,n=t.width-i,r=t.height-o,s=0,a=0)=>{if(i|=0,o|=0,r|=0,s|=0,a|=0,(n|=0)<=0||r<=0)return;let l=new Uint32Array(t.data.buffer),h=new Uint32Array(e.data.buffer);for(let d=0;d<r;d++){let r=o+d;if(r<0||r>=t.height)continue;let u=a+d;if(!(u<0||u>=e.height))for(let o=0;o<n;o++){let n=i+o;if(n<0||n>=t.width)continue;let a=s+o;if(a<0||a>=e.width)continue;let d=r*t.width+n;h[u*e.width+a]=l[d]}}}}}),St=At({"../../node_modules/.pnpm/@rgba-image+create-image@0.1.1/node_modules/@rgba-image/create-image/dist/index.js"(t){Object.defineProperty(t,"__esModule",{value:!0}),t.CreateImageFactory=(t=[0,0,0,0],e=4)=>{if(e=Math.floor(e),isNaN(e)||e<1)throw TypeError("channels should be a positive non-zero number");if(!("length"in t)||t.length<e)throw TypeError(`fill should be iterable with at least ${e} members`);let i=(t=new Uint8ClampedArray(t).slice(0,e)).every((t=>0===t));return(o,n,r)=>{if(void 0===o||void 0===n)throw TypeError("Not enough arguments");if(o=Math.floor(o),n=Math.floor(n),isNaN(o)||o<1||isNaN(n)||n<1)throw TypeError("Index or size is negative or greater than the allowed amount");let s=o*n*e;if(void 0===r&&(r=new Uint8ClampedArray(s)),r instanceof Uint8ClampedArray){if(r.length!==s)throw TypeError("Index or size is negative or greater than the allowed amount");if(!i)for(let i=0;i<n;i++)for(let n=0;n<o;n++){let s=(i*o+n)*e;for(let i=0;i<e;i++)r[s+i]=t[i]}return{get width(){return o},get height(){return n},get data(){return r}}}throw TypeError("Expected data to be Uint8ClampedArray or undefined")}},t.createImage=t.CreateImageFactory()}}),kt=At({"../../node_modules/.pnpm/@rgba-image+lanczos@0.1.1/node_modules/@rgba-image/lanczos/dist/filters.js"(t){Object.defineProperty(t,"__esModule",{value:!0}),t.filters=void 0;var e=(t,e)=>{if(t<=-e||t>=e||0==t)return 0;let i=t*Math.PI;return Math.sin(i)/i*Math.sin(i/e)/(i/e)},i=t=>Math.round(16383*t);t.filters=(t,o,n,r,s)=>{let a=s?2:3,l=1/n,h=Math.min(1,n),d=a/h,u=Math.floor(2*(d+1)),p=new Int16Array((u+2)*o),c=0;for(let n=0;n<o;n++){let s=(n+.5)*l+r,u=Math.max(0,Math.floor(s-d)),f=Math.min(t-1,Math.ceil(s+d)),m=f-u+1,v=new Float32Array(m),_=new Int16Array(m),y=0,g=0;for(let t=u;t<=f;t++){let i=e((t+.5-s)*h,a);y+=i,v[g]=i,g++}let b=0;for(let t=0;t<v.length;t++){let e=v[t]/y;b+=e,_[t]=i(e)}_[o>>1]+=i(1-b);let w=0;for(;w<_.length&&0===_[w];)w++;let A=_.length-1;for(;A>0&&0===_[A];)A--;let C=u+w,L=A-w+1;p[c++]=C,p[c++]=L,p.set(_.subarray(w,A+1),c),c+=L}return p}}}),xt=At({"../../node_modules/.pnpm/@rgba-image+lanczos@0.1.1/node_modules/@rgba-image/lanczos/dist/convolve.js"(t){Object.defineProperty(t,"__esModule",{value:!0}),t.convolve=void 0;t.convolve=(t,e,i,o,n,r)=>{let s=0,a=0;for(let l=0;l<o;l++){let h=0;for(let i=0;i<n;i++){let i=s+4*r[h++]|0,n=0,l=0,d=0,u=0;for(let e=r[h++];e>0;e--){let e=r[h++];n=n+e*t[i]|0,l=l+e*t[i+1]|0,d=d+e*t[i+2]|0,u=u+e*t[i+3]|0,i=i+4|0}e[a]=n+8192>>14,e[a+1]=l+8192>>14,e[a+2]=d+8192>>14,e[a+3]=u+8192>>14,a=a+4*o|0}a=4*(l+1)|0,s=(l+1)*i*4|0}}}}),$t=At({"../../node_modules/.pnpm/@rgba-image+lanczos@0.1.1/node_modules/@rgba-image/lanczos/dist/index.js"(t){Object.defineProperty(t,"__esModule",{value:!0}),t.lanczos2=t.lanczos=void 0;var e=Lt(),i=St(),o=kt(),n=xt(),r=(t,e,i=!1)=>{let r=e.width/t.width,s=e.height/t.height,a=o.filters(t.width,e.width,r,0,i),l=o.filters(t.height,e.height,s,0,i),h=new Uint8ClampedArray(e.width*t.height*4);n.convolve(t.data,h,t.width,t.height,e.width,a),n.convolve(h,e.data,t.height,e.width,e.height,l)};t.lanczos=(t,o,n=0,s=0,a=t.width-n,l=t.height-s,h=0,d=0,u=o.width-h,p=o.height-d)=>{if(s|=0,l|=0,h|=0,d|=0,u|=0,p|=0,(a|=0)<=0||l<=0||u<=0||p<=0)return;if(0===(n|=0)&&0===s&&a===t.width&&l===t.height&&0===h&&0===d&&u===o.width&&p===o.height)return void r(t,o);let c=i.createImage(a,l),f=i.createImage(u,p);e.copy(t,c,n,s),r(c,f),e.copy(f,o,0,0,f.width,f.height,h,d)};t.lanczos2=(t,o,n=0,s=0,a=t.width-n,l=t.height-s,h=0,d=0,u=o.width-h,p=o.height-d)=>{if(s|=0,l|=0,h|=0,d|=0,u|=0,p|=0,(a|=0)<=0||l<=0||u<=0||p<=0)return;if(0===(n|=0)&&0===s&&a===t.width&&l===t.height&&0===h&&0===d&&u===o.width&&p===o.height)return void r(t,o,!0);let c=i.createImage(a,l),f=i.createImage(u,p);e.copy(t,c,n,s),r(c,f,!0),e.copy(f,o,0,0,f.width,f.height,h,d)}}}),Ot=((mt=Ot||{}).Bounce="bounce",mt.Normal="normal",mt),Pt=function(t,e){return{schema:"native_enum",nativeEnum:t,async:!1,_parse:(i,o)=>Object.values(t).includes(i)?{output:i}:et(o,"type","native_enum",e||"Invalid type",i)}}(Ot),It=st({autoplay:at(ot()),defaultTheme:at(lt()),direction:at(ut([nt(1),nt(-1)])),hover:at(ot()),id:lt(),intermission:at(rt()),loop:at(ut([ot(),rt()])),playMode:at(Pt),speed:at(rt()),themeColor:at(lt())}),Mt=st({animations:it(lt()),id:lt()}),Et=st({activeAnimationId:at(lt()),animations:it(It),author:at(lt()),custom:at(function(t,e,i,o){let[n,r,s,a]=function(t,e,i,o){if("object"==typeof e&&!Array.isArray(e)){let[n,r]=tt(i,o);return[t,e,n,r]}let[n,r]=tt(e,i);return[lt(),t,n,r]}(t,e,i,o);return{schema:"record",record:{key:n,value:r},async:!1,_parse(t,e){if(!t||"object"!=typeof t)return et(e,"type","record",s||"Invalid type",t);let i,o={};for(let[s,a]of Object.entries(t))if(!ht.includes(s)){let l,h=n._parse(s,{origin:"key",abortEarly:e?.abortEarly,abortPipeEarly:e?.abortPipeEarly});if(h.issues){l={schema:"record",input:t,key:s,value:a};for(let t of h.issues)t.path=[l],i?.push(t);if(i||(i=h.issues),e?.abortEarly)break}let d=r._parse(a,e);if(d.issues){l=l||{schema:"record",input:t,key:s,value:a};for(let t of d.issues)t.path?t.path.unshift(l):t.path=[l],i?.push(t);if(i||(i=d.issues),e?.abortEarly)break}!h.issues&&!d.issues&&(o[h.output]=d.output)}return i?{issues:i}:Y(o,a,e,"record")}}}(lt(),function(t=[]){return{schema:"any",async:!1,_parse:(e,i)=>Y(e,t,i,"any")}}())),description:at(lt()),generator:at(lt()),keywords:at(lt()),revision:at(rt()),themes:at(it(Mt)),states:at(it(lt())),version:at(lt())}),Tt=function(t,e,i,o){let[n,r]=tt(i,o);return st(Object.entries(t.object).reduce(((t,[i,o])=>e.includes(i)?t:{...t,[i]:o}),{}),n,r)}(It,["id"]),Ut=st({state:lt()}),jt=Ut,Nt=pt([Ut,st({ms:rt()})]),zt=pt([Ut,st({count:rt()})]),Dt=Ut,Ft=Ut,Vt=Ut,Bt=pt([Ut,st({threshold:at(it(rt([ft(0),ct(1)])))})]),Ht=st({onAfter:at(Nt),onClick:at(jt),onComplete:at(Vt),onEnter:at(zt),onMouseEnter:at(Dt),onMouseLeave:at(Ft),onShow:at(Bt)}),qt=pt([Tt,st({playOnScroll:at(dt([rt([ft(0),ct(1)]),rt([ft(0),ct(1)])])),segments:at(ut([dt([rt(),rt()]),lt()]))})]);pt([Ht,st({animationId:at(lt()),playbackSettings:qt})]);var Rt={jpeg:"image/jpeg",png:"image/png",gif:"image/gif",bmp:"image/bmp",svg:"image/svg+xml",webp:"image/webp",mpeg:"audio/mpeg",mp3:"audio/mp3"},Zt={jpeg:[255,216,255],png:[137,80,78,71,13,10,26,10],gif:[71,73,70],bmp:[66,77],webp:[82,73,70,70,87,69,66,80],svg:[60,63,120],mp3:[73,68,51,3,0,0,0,0],mpeg:[73,68,51,3,0,0,0,0]},Jt=class extends Error{constructor(t,e){super(t),Ct(this,"code"),this.name="[dotlottie-js]",this.code=e}};function Wt(t){let e;if(typeof window>"u")e=Buffer.from(t).toString("base64");else{let i=Array.prototype.map.call(t,(t=>String.fromCharCode(t))).join("");e=window.btoa(i)}return`data:${(t=>{let e=null,i=[];if(!t)return null;let o=t.substring(t.indexOf(",")+1);e=typeof window>"u"?Buffer.from(o,"base64").toString("binary"):atob(o);let n=new Uint8Array(e.length);for(let t=0;t<e.length;t+=1)n[t]=e.charCodeAt(t);i=Array.from(n.subarray(0,8));for(let t in Zt){let e=Zt[t];if(e&&i.every(((t,i)=>t===e[i])))return Rt[t]}return null})(e)};base64,${e}`}function Kt(t){return"w"in t&&"h"in t&&!("xt"in t)&&"p"in t}function Gt(t){return!("h"in t)&&!("w"in t)&&"p"in t&&"e"in t&&"u"in t&&"id"in t}async function Xt(t,e=()=>!0){if(!(t instanceof Uint8Array))throw new Jt("DotLottie not found","INVALID_DOTLOTTIE");return await new Promise(((i,o)=>{W(t,{filter:e},((t,e)=>{t&&o(t),i(e)}))}))}async function Qt(t,e,i){if(!(t instanceof Uint8Array))throw new Jt("DotLottie not found","INVALID_DOTLOTTIE");return(await Xt(t,(t=>t.name===e&&(!i||i(t)))))[e]}async function Yt(t){let e="manifest.json",i=(await Xt(t,(t=>t.name===e)))[e];if(!(typeof i>"u"))return JSON.parse(R(i,!1))}async function te(t){if(!(t instanceof Uint8Array))return{success:!1,error:"DotLottie not found"};let e=await Yt(t);if(typeof e>"u")return{success:!1,error:"Invalid .lottie file, manifest.json is missing"};let i=function(t,e,i){let o=t._parse(e,i);return o.issues?{success:!1,error:new G(o.issues),issues:o.issues}:{success:!0,data:o.output,output:o.output}}(Et,e);return i.success?{success:!0}:{success:!1,error:`Invalid .lottie file, manifest.json structure is invalid, ${JSON.stringify(K(i.error).nested,null,2)}`}}async function ee(t){let e=new Uint8Array(t),i=await te(e);if(i.error)throw new Jt(i.error,"INVALID_DOTLOTTIE");return e}async function ie(t,e){let i=new Map;for(let[t,o]of Object.entries(e))for(let e of o.assets||[])if(Gt(e)){let o=e.p;i.has(o)||i.set(o,new Set),i.get(o)?.add(t)}let o=await async function(t,e){let i=await Xt(t,(t=>{let i=t.name.replace("audio/","");return t.name.startsWith("audio/")&&(!e||e({...t,name:i}))})),o={};for(let t in i){let e=i[t];e instanceof Uint8Array&&(o[t.replace("audio/","")]=Wt(e))}return o}(t,(t=>i.has(t.name)));for(let[t,n]of i){let i=o[t];if(i)for(let o of n){let n=e[o];for(let e of n?.assets||[])Gt(e)&&e.p===t&&(e.p=i,e.u="",e.e=1)}}}async function oe(t,e){let i=new Map;for(let[t,o]of Object.entries(e))for(let e of o.assets||[])if(Kt(e)){let o=e.p;i.has(o)||i.set(o,new Set),i.get(o)?.add(t)}let o=await async function(t,e){let i=await Xt(t,(t=>{let i=t.name.replace("images/","");return t.name.startsWith("images/")&&(!e||e({...t,name:i}))})),o={};for(let t in i){let e=i[t];e instanceof Uint8Array&&(o[t.replace("images/","")]=Wt(e))}return o}(t,(t=>i.has(t.name)));for(let[t,n]of i){let i=o[t];if(i)for(let o of n){let n=e[o];for(let e of n?.assets||[])Kt(e)&&e.p===t&&(e.p=i,e.u="",e.e=1)}}}function ne(t,e="dotLottie-common"){return new Error(`[${e}]: ${t}`)}function re(t,e="dotLottie-common",...i){}function se(t,e="dotLottie-common",...i){}function ae(t){return["v","ip","op","layers","fr","w","h"].every((e=>Object.prototype.hasOwnProperty.call(t,e)))}function le(t,e){let i=Object.keys(t).find((i=>t[i]===e));if(void 0===i)throw new Error("Value not found in the object.");return i}((t,e,i)=>{i=null!=t?vt(bt(t)):{},((t,e,i,o)=>{if(e&&"object"==typeof e||"function"==typeof e)for(let n of gt(e))!wt.call(t,n)&&n!==i&&_t(t,n,{get:()=>e[n],enumerable:!(o=yt(e,n))||o.enumerable})})(!e&&t&&t.__esModule?i:_t(i,"default",{value:t,enumerable:!0}),t)})($t());var he=class{constructor(){(0,o.A)(this,"_dotLottie",void 0),(0,o.A)(this,"_animationsMap",new Map),(0,o.A)(this,"_themeMap",new Map),(0,o.A)(this,"_stateMachinesMap",new Map),(0,o.A)(this,"_manifest",void 0)}get dotLottie(){return this._dotLottie}get animationsMap(){return this._animationsMap}get themeMap(){return this._themeMap}get stateMachinesMap(){return this._stateMachinesMap}get manifest(){return this._manifest}async loadFromUrl(t){let e=await fetch(t,{method:"GET",mode:"cors"});if(!e.ok)throw new Error(`Failed to load dotLottie from ${t} with status ${e.status}`);if(e.headers.get("content-type")?.includes("application/json")){let i=await e.json();if(!ae(i))throw new Error(`Invalid lottie JSON at ${t}`);let o=function(t=""){let e=t.trim(),i=e.lastIndexOf("/"),o=e.substring(i+1),n=o.indexOf(".");return-1!==n?o.substring(0,n):o}(t);this._animationsMap.set(o,i);let n={activeAnimationId:o,animations:[{id:o}]};this._manifest=n}else{this._dotLottie=await ee(await e.arrayBuffer());let t=await Yt(this._dotLottie);if(!t)throw new Error("Manifest not found");this._manifest=t}}loadFromLottieJSON(t){if(!ae(t))throw new Error("Invalid lottie JSON");let e="my-animation";this._animationsMap.set(e,t);let i={activeAnimationId:e,animations:[{id:e}]};this._manifest=i}async loadFromArrayBuffer(t){this._dotLottie=await ee(t);let e=await Yt(this._dotLottie);if(!e)throw new Error("Manifest not found");this._manifest=e}async getAnimation(t){if(this._animationsMap.get(t))return this._animationsMap.get(t);if(!this._dotLottie)return;let e=await async function(t,e,{inlineAssets:i}={},o){let n=`animations/${e}.json`,r=await Qt(t,n,o);if(typeof r>"u")return;let s=JSON.parse(R(r,!1));if(!i)return s;let a={[e]:s};return await oe(t,a),await ie(t,a),s}(this._dotLottie,t,{inlineAssets:!0});return e&&this._animationsMap.set(t,e),e}async getTheme(t){if(this._themeMap.get(t))return this._themeMap.get(t);if(!this._dotLottie)return;let e=await async function(t,e,i){let o=`themes/${e}.lss`,n=await Qt(t,o,i);if(!(typeof n>"u"))return R(n,!1)}(this._dotLottie,t);return e&&this._themeMap.set(t,e),e}async getStateMachines(){if(!this._dotLottie)return;let t=await async function(t,e){let i={},o=await Xt(t,(t=>{let i=t.name.replace("states/","").replace(".json","");return t.name.startsWith("states/")&&(!e||e({...t,name:i}))}));for(let t in o){let e=o[t];e instanceof Uint8Array&&(i[t.replace("states/","").replace(".json","")]=R(e,!1))}return i}(this._dotLottie);for(let e in t)if(e){let i=t[e];if(i){let t=JSON.parse(i);if(t){let e=t.descriptor.id;this._stateMachinesMap.get(e)||this._stateMachinesMap.set(e,t)}}}return Array.from(this._stateMachinesMap.values())}async getStateMachine(t){if(this._stateMachinesMap.get(t))return this._stateMachinesMap.get(t);if(!this._dotLottie)return;let e=await async function(t,e,i){let o=`states/${e}.json`,n=await Qt(t,o,i);return typeof n>"u"?void 0:JSON.parse(R(n,!1))}(this._dotLottie,t);return e&&this._stateMachinesMap.set(e.descriptor.id,e),e}};function de(){throw new Error("Cycle detected")}function ue(){if(fe>1)fe--;else{for(var t,e=!1;void 0!==ce;){var i=ce;for(ce=void 0,me++;void 0!==i;){var o=i.o;if(i.o=void 0,i.f&=-3,!(8&i.f)&&ge(i))try{i.c()}catch(i){e||(t=i,e=!0)}i=o}}if(me=0,fe--,e)throw t}}var pe=void 0,ce=void 0,fe=0,me=0,ve=0;function _e(t){if(void 0!==pe){var e=t.n;if(void 0===e||e.t!==pe)return e={i:0,S:t,p:pe.s,n:void 0,t:pe,e:void 0,x:void 0,r:e},void 0!==pe.s&&(pe.s.n=e),pe.s=e,t.n=e,32&pe.f&&t.S(e),e;if(-1===e.i)return e.i=0,void 0!==e.n&&(e.n.p=e.p,void 0!==e.p&&(e.p.n=e.n),e.p=pe.s,e.n=void 0,pe.s.n=e,pe.s=e),e}}function ye(t){this.v=t,this.i=0,this.n=void 0,this.t=void 0}function ge(t){for(var e=t.s;void 0!==e;e=e.n)if(e.S.i!==e.i||!e.S.h()||e.S.i!==e.i)return!0;return!1}function be(t){for(var e=t.s;void 0!==e;e=e.n){var i=e.S.n;if(void 0!==i&&(e.r=i),e.S.n=e,e.i=-1,void 0===e.n){t.s=e;break}}}function we(t){for(var e=t.s,i=void 0;void 0!==e;){var o=e.p;-1===e.i?(e.S.U(e),void 0!==o&&(o.n=e.n),void 0!==e.n&&(e.n.p=o)):i=e,e.S.n=e.r,void 0!==e.r&&(e.r=void 0),e=o}t.s=i}function Ae(t){ye.call(this,void 0),this.x=t,this.s=void 0,this.g=ve-1,this.f=4}function Ce(t){var e=t.u;if(t.u=void 0,"function"==typeof e){fe++;var i=pe;pe=void 0;try{e()}catch(e){throw t.f&=-2,t.f|=8,Le(t),e}finally{pe=i,ue()}}}function Le(t){for(var e=t.s;void 0!==e;e=e.n)e.S.U(e);t.x=void 0,t.s=void 0,Ce(t)}function Se(t){if(pe!==this)throw new Error("Out-of-order effect");we(this),pe=t,this.f&=-2,8&this.f&&Le(this),ue()}function ke(t){this.x=t,this.u=void 0,this.s=void 0,this.o=void 0,this.f=32}ye.prototype.h=function(){return!0},ye.prototype.S=function(t){this.t!==t&&void 0===t.e&&(t.x=this.t,void 0!==this.t&&(this.t.e=t),this.t=t)},ye.prototype.U=function(t){if(void 0!==this.t){var e=t.e,i=t.x;void 0!==e&&(e.x=i,t.e=void 0),void 0!==i&&(i.e=e,t.x=void 0),t===this.t&&(this.t=i)}},ye.prototype.subscribe=function(t){var e=this;return function(t){var e=new ke(t);try{e.c()}catch(t){throw e.d(),t}return e.d.bind(e)}((function(){var i=e.value,o=32&this.f;this.f&=-33;try{t(i)}finally{this.f|=o}}))},ye.prototype.valueOf=function(){return this.value},ye.prototype.toString=function(){return this.value+""},ye.prototype.toJSON=function(){return this.value},ye.prototype.peek=function(){return this.v},Object.defineProperty(ye.prototype,"value",{get:function(){var t=_e(this);return void 0!==t&&(t.i=this.i),this.v},set:function(t){if(pe instanceof Ae&&function(){throw new Error("Computed cannot have side-effects")}(),t!==this.v){me>100&&de(),this.v=t,this.i++,ve++,fe++;try{for(var e=this.t;void 0!==e;e=e.x)e.t.N()}finally{ue()}}}}),(Ae.prototype=new ye).h=function(){if(this.f&=-3,1&this.f)return!1;if(32==(36&this.f)||(this.f&=-5,this.g===ve))return!0;if(this.g=ve,this.f|=1,this.i>0&&!ge(this))return this.f&=-2,!0;var t=pe;try{be(this),pe=this;var e=this.x();(16&this.f||this.v!==e||0===this.i)&&(this.v=e,this.f&=-17,this.i++)}catch(t){this.v=t,this.f|=16,this.i++}return pe=t,we(this),this.f&=-2,!0},Ae.prototype.S=function(t){if(void 0===this.t){this.f|=36;for(var e=this.s;void 0!==e;e=e.n)e.S.S(e)}ye.prototype.S.call(this,t)},Ae.prototype.U=function(t){if(void 0!==this.t&&(ye.prototype.U.call(this,t),void 0===this.t)){this.f&=-33;for(var e=this.s;void 0!==e;e=e.n)e.S.U(e)}},Ae.prototype.N=function(){if(!(2&this.f)){this.f|=6;for(var t=this.t;void 0!==t;t=t.x)t.t.N()}},Ae.prototype.peek=function(){if(this.h()||de(),16&this.f)throw this.v;return this.v},Object.defineProperty(Ae.prototype,"value",{get:function(){1&this.f&&de();var t=_e(this);if(this.h(),void 0!==t&&(t.i=this.i),16&this.f)throw this.v;return this.v}}),ke.prototype.c=function(){var t=this.S();try{if(8&this.f||void 0===this.x)return;var e=this.x();"function"==typeof e&&(this.u=e)}finally{t()}},ke.prototype.S=function(){1&this.f&&de(),this.f|=1,this.f&=-9,Ce(this),be(this),fe++;var t=pe;return pe=this,Se.bind(this,t)},ke.prototype.N=function(){2&this.f||(this.f|=2,this.o=ce,ce=this)},ke.prototype.d=function(){this.f|=8,1&this.f||Le(this)};var xe=class{constructor(t){(0,o.A)(this,"_state",void 0),(0,o.A)(this,"_prevState",void 0),this._prevState=t,this._state=function(t){return new ye(t)}(t)}setState(t){this._prevState=this._state.value,this._state.value=t}subscribe(t){return this._state.subscribe((e=>t(e,this._prevState)))}};var $e={"@dotlottie/dotlottie-js":"0.6.0","@lottiefiles/relottie":"1.0.0","@lottiefiles/relottie-style":"0.4.1","@preact/signals-core":"^1.2.3",howler:"^2.2.3","lottie-web":"^5.12.2",xstate:"^4.38.1"},Oe=(t=>(t.Complete="complete",t.DataFail="data_fail",t.DataReady="data_ready",t.Error="error",t.Frame="frame",t.Freeze="freeze",t.LoopComplete="loopComplete",t.Pause="pause",t.Play="play",t.Ready="ready",t.Stop="stop",t.VisibilityChange="visibilityChange",t))(Oe||{}),Pe=(t=>(t.Completed="completed",t.Error="error",t.Fetching="fetching",t.Frozen="frozen",t.Initial="initial",t.Loading="loading",t.Paused="paused",t.Playing="playing",t.Ready="ready",t.Stopped="stopped",t))(Pe||{}),Ie=(t=>(t.Bounce="bounce",t.Normal="normal",t))(Ie||{}),Me={autoplay:!1,direction:1,hover:!1,intermission:0,loop:!1,playMode:"normal",speed:1,defaultTheme:""},Ee={activeStateId:"",autoplay:!1,currentState:"initial",frame:0,seeker:0,direction:1,hover:!1,loop:!1,playMode:"normal",speed:1,background:"transparent",intermission:0,currentAnimationId:void 0,visibilityPercentage:0},Te=class{constructor(t,e,i){(0,o.A)(this,"_lottie",void 0),(0,o.A)(this,"_src",void 0),(0,o.A)(this,"_animationConfig",void 0),(0,o.A)(this,"_prevUserPlaybackOptions",{}),(0,o.A)(this,"_userPlaybackOptions",void 0),(0,o.A)(this,"_hover",!1),(0,o.A)(this,"_loop",!1),(0,o.A)(this,"_counter",0),(0,o.A)(this,"_intermission",0),(0,o.A)(this,"_counterInterval",null),(0,o.A)(this,"_container",null),(0,o.A)(this,"_name",void 0),(0,o.A)(this,"_mode","normal"),(0,o.A)(this,"_background","transparent"),(0,o.A)(this,"_animation",void 0),(0,o.A)(this,"_defaultTheme",void 0),(0,o.A)(this,"_activeAnimationId",void 0),(0,o.A)(this,"_currentAnimationId",void 0),(0,o.A)(this,"_testId",void 0),(0,o.A)(this,"_listeners",new Map),(0,o.A)(this,"_currentState","initial"),(0,o.A)(this,"_stateBeforeFreeze","initial"),(0,o.A)(this,"state",new xe(Ee)),(0,o.A)(this,"_light",!1),(0,o.A)(this,"_worker",!1),(0,o.A)(this,"_dotLottieLoader",new he),(0,o.A)(this,"_activeStateId",void 0),(0,o.A)(this,"_inInteractiveMode",!1),(0,o.A)(this,"_scrollTicking",!1),(0,o.A)(this,"_scrollCallback",void 0),(0,o.A)(this,"_onShowIntersectionObserver",void 0),(0,o.A)(this,"_visibilityPercentage",0),(0,o.A)(this,"_audios",[]),(0,o.A)(this,"_stateMachineManager",void 0),this._src=structuredClone(t),i?.testId&&(this._testId=i.testId),this._defaultTheme=i?.defaultTheme||"",this._userPlaybackOptions=this._validatePlaybackOptions(i||{}),"string"==typeof i?.activeAnimationId&&(this._activeAnimationId=i.activeAnimationId),this._container=e||null,"string"==typeof i?.background&&this.setBackground(i.background),typeof i?.activeStateId<"u"&&(this._activeStateId=i.activeStateId);let{rendererSettings:n,...r}=i||{};this._animationConfig={loop:!1,autoplay:!1,renderer:"svg",rendererSettings:{clearCanvas:!0,progressiveLoad:!0,hideOnTransparent:!0,filterSize:{width:"200%",height:"200%",x:"-50%",y:"-50%"},...n},...r},i?.light&&(this._light=i.light),i?.worker&&(this._worker=i.worker),this._listenToHover(),this._listenToVisibilityChange()}_listenToHover(){let t=()=>{this._hover&&"playing"!==this.currentState&&this.play()},e=()=>{this._hover&&"playing"===this.currentState&&this.stop()};this._container?.removeEventListener("mouseenter",t),this._container?.removeEventListener("mouseleave",e),this._container?.addEventListener("mouseleave",e),this._container?.addEventListener("mouseenter",t)}_onVisibilityChange(){!this._lottie||typeof document>"u"||(document.hidden&&"playing"===this.currentState?this.freeze():"frozen"===this.currentState&&this.unfreeze())}_listenToVisibilityChange(){typeof document<"u"&&typeof document.hidden<"u"&&document.addEventListener("visibilitychange",(()=>this._onVisibilityChange()))}_getOption(t){if(typeof this._userPlaybackOptions[t]<"u")return this._userPlaybackOptions[t];let e=this._dotLottieLoader.manifest?.animations.find((t=>t.id===this._currentAnimationId));return e&&typeof e[t]<"u"?e[t]:Me[t]}_getPlaybackOptions(){let t={};for(let e in Me)typeof Me[e]<"u"&&(t[e]=this._getOption(e));return t}_setPlayerState(t){let e=t(this._getPlaybackOptions());try{Tt._parse(e)}catch{return void JSON.stringify(e,null,2)}typeof e.defaultTheme<"u"&&(this._defaultTheme=e.defaultTheme),typeof e.playMode<"u"&&(this._mode=e.playMode),typeof e.intermission<"u"&&(this._intermission=e.intermission),typeof e.hover<"u"&&(this._hover=e.hover),typeof e.loop<"u"&&(this.clearCountTimer(),this._loop=e.loop,this._counter=0,this._lottie?.setLoop("number"==typeof e.loop||e.loop)),typeof e.speed<"u"&&this._lottie?.setSpeed(e.speed),typeof e.autoplay<"u"&&this._lottie&&(this._lottie.autoplay=e.autoplay),typeof e.direction<"u"&&this._lottie?.setDirection(e.direction)}_getOptionsFromAnimation(t){let{id:e,...i}=t;return{...Me,...i}}_updateTestData(){!this._testId||!this._lottie||(window.dotLottiePlayer||(window.dotLottiePlayer={[this._testId]:{}}),window.dotLottiePlayer[this._testId]={direction:this._lottie.playDirection,currentState:this._currentState,loop:this.loop,mode:this._mode,speed:this._lottie.playSpeed})}setContainer(t){t!==this._container&&(this._container=t,this.setBackground(this._background),this._listenToHover())}get currentState(){return this._currentState}clearCountTimer(){this._counterInterval&&clearInterval(this._counterInterval)}setCurrentState(t){this._currentState=t,this._notify(),this._updateTestData()}static isPathJSON(t){return"json"===t.split(".").pop()?.toLowerCase()}get src(){return this._src}updateSrc(t){this._src!==t&&(this._src=structuredClone(t),this._activeAnimationId=void 0,this._currentAnimationId=void 0,this.load())}get intermission(){return this._intermission}get hover(){return this._hover}setHover(t){"boolean"==typeof t&&(this._hover=t,this._userPlaybackOptions.hover=t,this._notify())}setIntermission(t){this._intermission=t,this._userPlaybackOptions.intermission=t,this._notify()}get mode(){return this._mode}get animations(){return this._dotLottieLoader.animationsMap}get themes(){return this._dotLottieLoader.themeMap}setMode(t){"string"==typeof t&&(this._mode=t,this._userPlaybackOptions.playMode=t,this._setPlayerState((()=>({playMode:t}))),this._notify(),this._updateTestData())}get container(){if(this._container)return this._container}goToAndPlay(t,e,i){this._lottie&&!["loading"].includes(this._currentState)&&(this._lottie.goToAndPlay(t,e,i),this.setCurrentState("playing"))}goToAndStop(t,e,i){this._lottie&&!["loading"].includes(this._currentState)&&(this._lottie.goToAndStop(t,e,i),this.setCurrentState("stopped"))}seek(t){if(!this._lottie||["loading"].includes(this._currentState))return;let e=t;"number"==typeof e&&(e=Math.round(e));let i=/^(\d+)(%?)$/u.exec(e.toString());if(!i)return;let o="%"===i[2]?this.totalFrames*Number(i[1])/100:i[1];void 0!==o&&(this._lottie.goToAndPlay(o,!0),"playing"===this.currentState?this.play():"frozen"===this.currentState?this.freeze():this.pause())}_areNumbersInRange(t,e){return t>=0&&t<=1&&e>=0&&e<=1}_updatePosition(t,e,i){let[o,n]=t??[0,this.totalFrames-1],[r,s]=e??[0,1];if(this._areNumbersInRange(r,s)){if(this.container){let{height:t,top:e}=this.container.getBoundingClientRect(),a=(window.innerHeight-e)/(window.innerHeight+t),l=o+Math.round((a-r)/(s-r)*(n-o));i&&i(a),this.goToAndStop(l,!0),(l>=n||a>=s)&&this._handleAnimationComplete()}this._scrollTicking=!1}}_requestTick(t,e,i){this._scrollTicking||(requestAnimationFrame((()=>this._updatePosition(t,e,i))),this._scrollTicking=!0)}playOnScroll(t){this.stop(),this._scrollCallback&&this.stopPlayOnScroll(),this._scrollCallback=()=>this._requestTick(t?.segments,t?.threshold,t?.positionCallback),window.addEventListener("scroll",this._scrollCallback)}stopPlayOnScroll(){this._scrollCallback&&(window.removeEventListener("scroll",this._scrollCallback),this._scrollCallback=void 0)}stopPlayOnShow(){this._onShowIntersectionObserver&&(this._onShowIntersectionObserver.disconnect(),this._onShowIntersectionObserver=void 0)}addIntersectionObserver(t){if(!this.container)throw ne("Can't play on show, player container element not available.");let e={root:null,rootMargin:"0px",threshold:t?.threshold?t.threshold:[0,1]};this._onShowIntersectionObserver=new IntersectionObserver((e=>{e.forEach((e=>{this._visibilityPercentage=100*e.intersectionRatio,e.isIntersecting?(t?.callbackOnIntersect&&t.callbackOnIntersect(this._visibilityPercentage),this._container?.dispatchEvent(new Event("visibilityChange"))):t?.callbackOnIntersect&&(t.callbackOnIntersect(0),this._container?.dispatchEvent(new Event("visibilityChange")))}))}),e),this._onShowIntersectionObserver.observe(this.container)}playOnShow(t){if(this.stop(),!this.container)throw ne("Can't play on show, player container element not available.");this._onShowIntersectionObserver&&this.stopPlayOnShow(),this.addIntersectionObserver({threshold:t?.threshold??[],callbackOnIntersect:t=>{0===t?this.pause():this.play()}})}_validatePlaybackOptions(t){if(!t)return{};let e={};for(let[i,o]of Object.entries(t))switch(i){case"autoplay":"boolean"==typeof o&&(e.autoplay=o);break;case"direction":"number"==typeof o&&[1,-1].includes(o)&&(e.direction=o);break;case"loop":("boolean"==typeof o||"number"==typeof o)&&(e.loop=o);break;case"playMode":"string"==typeof o&&["normal","bounce"].includes(o)&&(e.playMode=o);break;case"speed":"number"==typeof o&&(e.speed=o);break;case"themeColor":"string"==typeof o&&(e.themeColor=o);break;case"hover":"boolean"==typeof o&&(e.hover=o);break;case"intermission":"number"==typeof o&&(e.intermission=o);break;case"defaultTheme":"string"==typeof o&&(e.defaultTheme=o)}return this._requireValidPlaybackOptions(e),e}_requireAnimationsInTheManifest(){if(!this._dotLottieLoader.manifest?.animations.length)throw ne("No animations found in manifest.")}_requireAnimationsToBeLoaded(){if(0===this._dotLottieLoader.animationsMap.size)throw ne("No animations have been loaded.")}async play(t,e){if(!["initial","loading"].includes(this._currentState)){if(this._requireAnimationsInTheManifest(),this._requireAnimationsToBeLoaded(),this._lottie&&!t)return-1===this._lottie.playDirection&&0===this._lottie.currentFrame?this._lottie.goToAndPlay(this._lottie.totalFrames,!0):this._lottie.play(),void this.setCurrentState("playing");if("number"==typeof t){let i=this._dotLottieLoader.manifest?.animations[t];if(!i)throw ne("animation not found.");"function"==typeof e?await this.render({id:i.id,...e(this._getPlaybackOptions(),this._getOptionsFromAnimation(i))}):await this.render({id:i.id})}if("string"==typeof t){let i=this._dotLottieLoader.manifest?.animations.find((e=>e.id===t));if(!i)throw ne("animation not found.");"function"==typeof e?await this.render({id:i.id,...e(this._getPlaybackOptions(),this._getOptionsFromAnimation(i))}):await this.render({id:i.id})}}}playSegments(t,e){this._lottie&&!["loading"].includes(this._currentState)&&(this._lottie.playSegments(t,e),this.setCurrentState("playing"))}resetSegments(t){this._lottie&&!["loading"].includes(this._currentState)&&this._lottie.resetSegments(t)}togglePlay(){"playing"===this.currentState?this.pause():this.play()}_getAnimationByIdOrIndex(t){if(this._requireAnimationsInTheManifest(),this._requireAnimationsToBeLoaded(),"number"==typeof t){let e=this._dotLottieLoader.manifest?.animations[t];if(!e)throw ne("animation not found.");return e}if("string"==typeof t){let e=this._dotLottieLoader.manifest?.animations.find((e=>e.id===t));if(!e)throw ne("animation not found.");return e}throw ne("first param must be a number or string")}get activeAnimationId(){return this._getActiveAnimationId()}get currentAnimationId(){return this._currentAnimationId}get activeStateId(){return this._activeStateId}async _startInteractivity(t){if(this._inInteractiveMode){if(0===this._dotLottieLoader.stateMachinesMap.size&&await this._dotLottieLoader.getStateMachines(),0===this._dotLottieLoader.stateMachinesMap.size)throw ne("No interactivity states are available.");if("undefined"===t)throw ne("stateId is not specified.");this._stateMachineManager||(this._stateMachineManager=await async function(t,e){let[{DotLottieStateMachineManager:o}]=await Promise.all([i.e(453).then(i.bind(i,5453))]);if(!t.length)throw ne("No state machines available inside this .lottie!");return new o(t,e)}(Array.from(this._dotLottieLoader.stateMachinesMap.values()),this)),this._stateMachineManager.start(t)}}enterInteractiveMode(t){if(!t)throw ne("stateId must be a non-empty string.");this._inInteractiveMode||(this._prevUserPlaybackOptions={...this._userPlaybackOptions}),this._inInteractiveMode&&this._stateMachineManager?.stop(),this._activeStateId=t,this._inInteractiveMode=!0,this._startInteractivity(t)}exitInteractiveMode(){this._inInteractiveMode&&(this._inInteractiveMode=!1,this._activeStateId="",this._stateMachineManager?.stop(),this._userPlaybackOptions={},this._userPlaybackOptions={...this._prevUserPlaybackOptions},this._prevUserPlaybackOptions={},this.reset())}reset(){let t=this._getActiveAnimationId(),e=this._dotLottieLoader.manifest?.animations.find((e=>e.id===t));if(this._inInteractiveMode&&this.exitInteractiveMode(),!e)throw ne("animation not found.");this.play(t)}previous(t){if(!this._dotLottieLoader.manifest||!this._dotLottieLoader.manifest.animations.length)throw ne("manifest not found.");if(this._inInteractiveMode)return;let e=this._dotLottieLoader.manifest.animations.findIndex((t=>t.id===this._currentAnimationId));if(-1===e)throw ne("animation not found.");let i=this._dotLottieLoader.manifest.animations[(e-1+this._dotLottieLoader.manifest.animations.length)%this._dotLottieLoader.manifest.animations.length];if(!i||!i.id)throw ne("animation not found.");"function"==typeof t?this.render({id:i.id,...t(this._getPlaybackOptions(),this._getOptionsFromAnimation(i))}):this.render({id:i.id})}next(t){if(!this._dotLottieLoader.manifest||!this._dotLottieLoader.manifest.animations.length)throw ne("manifest not found.");if(this._inInteractiveMode)return;let e=this._dotLottieLoader.manifest.animations.findIndex((t=>t.id===this._currentAnimationId));if(-1===e)throw ne("animation not found.");let i=this._dotLottieLoader.manifest.animations[(e+1)%this._dotLottieLoader.manifest.animations.length];if(!i||!i.id)throw ne("animation not found.");"function"==typeof t?this.render({id:i.id,...t(this._getPlaybackOptions(),this._getOptionsFromAnimation(i))}):this.render({id:i.id})}getManifest(){return this._dotLottieLoader.manifest}resize(){this._lottie&&!["loading"].includes(this._currentState)&&this._lottie.resize()}stop(){this._lottie&&!["loading"].includes(this._currentState)&&(this.clearCountTimer(),this._counter=0,this._setPlayerState((()=>({direction:this._getOption("direction")}))),this._lottie.stop(),this.setCurrentState("stopped"))}pause(){this._lottie&&!["loading"].includes(this._currentState)&&(this.clearCountTimer(),this._lottie.pause(),this.setCurrentState("paused"))}freeze(){this._lottie&&!["loading"].includes(this._currentState)&&("frozen"!==this.currentState&&(this._stateBeforeFreeze=this.currentState),this._lottie.pause(),this.setCurrentState("frozen"))}unfreeze(){this._lottie&&!["loading"].includes(this._currentState)&&("playing"===this._stateBeforeFreeze?this.play():this.pause())}destroy(){this._container?.__lottie&&(this._container.__lottie.destroy(),this._container.__lottie=null),this._audios.length&&(this._audios.forEach((t=>{t.unload()})),this._audios=[]),this.clearCountTimer(),typeof document<"u"&&document.removeEventListener("visibilitychange",(()=>this._onVisibilityChange())),this._counter=0,this._lottie?.destroy(),this._lottie=void 0}getAnimationInstance(){return this._lottie}static getLottieWebVersion(){return`${$e["lottie-web"]}`}addEventListener(t,e){this._listeners.has(t)||this._listeners.set(t,new Set),this._listeners.get(t)?.add(e);try{"complete"===t?this._container?.addEventListener(t,e):this._lottie?.addEventListener(t,e)}catch(t){}}getState(){return{autoplay:this._lottie?.autoplay??!1,currentState:this._currentState,frame:this._frame,visibilityPercentage:this._visibilityPercentage,seeker:this._seeker,direction:this._lottie?.playDirection??1,hover:this._hover,loop:this._loop||!1,playMode:this._mode,speed:this._lottie?.playSpeed??1,background:this._background,intermission:this._intermission,defaultTheme:this._defaultTheme,currentAnimationId:this._currentAnimationId,activeStateId:this._activeStateId??""}}_notify(){this.state.setState(this.getState())}get totalFrames(){return this._lottie?.totalFrames||0}get direction(){return this._lottie?this._lottie.playDirection:1}setDirection(t){this._requireValidDirection(t),this._setPlayerState((()=>({direction:t}))),this._userPlaybackOptions.direction=t}get speed(){return this._lottie?.playSpeed||1}setSpeed(t){this._requireValidSpeed(t),this._setPlayerState((()=>({speed:t}))),this._userPlaybackOptions.speed=t}get autoplay(){return this._lottie?.autoplay??!1}setAutoplay(t){this._requireValidAutoplay(t),this._lottie&&!["loading"].includes(this._currentState)&&(this._setPlayerState((()=>({autoplay:t}))),this._userPlaybackOptions.autoplay=t)}toggleAutoplay(){this._lottie&&!["loading"].includes(this._currentState)&&this.setAutoplay(!this._lottie.autoplay)}get defaultTheme(){return this._defaultTheme}setDefaultTheme(t){this._setPlayerState((()=>({defaultTheme:t}))),this._userPlaybackOptions.defaultTheme=t,this._animation&&this.render()}get loop(){return this._loop}setLoop(t){this._requireValidLoop(t),this._setPlayerState((()=>({loop:t}))),this._userPlaybackOptions.loop=t}toggleLoop(){this._lottie&&!["loading"].includes(this._currentState)&&this.setLoop(!this._loop)}get background(){return this._background}setBackground(t){this._requireValidBackground(t),this._background=t,this._container&&(this._container.style.backgroundColor=t)}get _frame(){return this._lottie?"completed"===this.currentState?-1===this.direction?0:this._lottie.totalFrames:this._lottie.currentFrame:0}get _seeker(){return this._lottie?this._frame/this._lottie.totalFrames*100:0}async revertToManifestValues(t){let e;e=Array.isArray(t)&&0!==t.length?t:["autoplay","defaultTheme","direction","hover","intermission","loop","playMode","speed","activeAnimationId"];let i=!1;if(e.includes("activeAnimationId")){let t=this._dotLottieLoader.manifest?.activeAnimationId,e=this._getAnimationByIdOrIndex(t||0);this._activeAnimationId=t,await this._setCurrentAnimation(e.id),i=!0}e.forEach((t=>{switch(t){case"autoplay":delete this._userPlaybackOptions.autoplay,this.setAutoplay(this._getOption("autoplay"));break;case"defaultTheme":delete this._userPlaybackOptions.defaultTheme,this.setDefaultTheme(this._getOption("defaultTheme"));break;case"direction":delete this._userPlaybackOptions.direction,this.setDirection(this._getOption("direction"));break;case"hover":delete this._userPlaybackOptions.hover,this.setHover(this._getOption("hover"));break;case"intermission":delete this._userPlaybackOptions.intermission,this.setIntermission(this._getOption("intermission"));break;case"loop":delete this._userPlaybackOptions.loop,this.setLoop(this._getOption("loop"));break;case"playMode":delete this._userPlaybackOptions.playMode,this.setMode(this._getOption("playMode")),this.setDirection(this._getOption("direction"));break;case"speed":delete this._userPlaybackOptions.speed,this.setSpeed(this._getOption("speed"))}})),i&&this.render()}removeEventListener(t,e){try{"complete"===t?this._container?.removeEventListener(t,e):this._lottie?.removeEventListener(t,e),this._listeners.get(t)?.delete(e)}catch(t){}}_handleAnimationComplete(){"number"==typeof this._loop&&this.stop();let t=-1===this.direction?0:this.totalFrames;this.goToAndStop(t,!0),this._counter=0,this.clearCountTimer(),this.setCurrentState("completed"),this._container?.dispatchEvent(new Event("complete"))}addEventListeners(){if(this._lottie&&!["loading"].includes(this._currentState)){this._lottie.addEventListener("enterFrame",(()=>{this._lottie&&(0===Math.floor(this._lottie.currentFrame)&&-1===this.direction&&(this._container?.dispatchEvent(new Event("complete")),this.loop||this.setCurrentState("completed")),this._notify())})),this._lottie.addEventListener("loopComplete",(()=>{if(!this._lottie)return;this._container?.dispatchEvent(new Event("loopComplete")),this.intermission>0&&this.pause();let t=this._lottie.playDirection;if("number"==typeof this._loop&&this._loop>0&&(this._counter+="bounce"===this._mode?.5:1,this._counter>=this._loop))return void this._handleAnimationComplete();"bounce"===this._mode&&"number"==typeof t&&(t=-1*Number(t));let e=-1===t?this._lottie.totalFrames-1:0;this.intermission?(this.goToAndPlay(e,!0),this.pause(),this._counterInterval=window.setTimeout((()=>{this._lottie&&(this._setPlayerState((()=>({direction:t}))),this.goToAndPlay(e,!0))}),this._intermission)):(this._setPlayerState((()=>({direction:t}))),this.goToAndPlay(-1===t?this.totalFrames-1:0,!0))})),this._lottie.addEventListener("complete",(()=>{if(this._lottie&&!1===this._loop&&"bounce"===this._mode){if(this._counter+=.5,this._counter>=1)return void this._handleAnimationComplete();this._counterInterval=window.setTimeout((()=>{if(!this._lottie)return;let t=this._lottie.playDirection;"bounce"===this._mode&&"number"==typeof t&&(t=-1*Number(t));let e=-1===t?this.totalFrames-1:0;this._setPlayerState((()=>({direction:t}))),this.goToAndPlay(e,!0)}),this._intermission)}else this._handleAnimationComplete()}));for(let[t,e]of this._listeners)if("complete"===t)for(let i of e)this._container?.addEventListener(t,i);else for(let i of e)this._lottie.addEventListener(t,i)}}async _setCurrentAnimation(t){this._currentState="loading";let e=await this._dotLottieLoader.getAnimation(t);this._currentAnimationId=t,this._animation=e,this._currentState="ready"}async _getAudioFactory(){if(this._animation&&function(t){let e=t.assets;return!!e&&e.some((t=>Gt(t)))}(this._animation)){let{DotLottieAudio:t}=await i.e(856).then(i.bind(i,8856));return e=>{let i=new t({src:[e]});return this._audios.push(i),i}}return null}async render(t){if(t?.id)await this._setCurrentAnimation(t.id);else if(!this._animation)throw ne("no animation selected");let e=Me.loop??!1,o=Me.autoplay??!1,n=Me.playMode??"normal",r=Me.intermission??0,s=Me.hover??!1,a=Me.direction??1,l=Me.speed??1,h=Me.defaultTheme??"";e=t?.loop??this._getOption("loop"),o=t?.autoplay??this._getOption("autoplay"),n=t?.playMode??this._getOption("playMode"),r=t?.intermission??this._getOption("intermission"),s=t?.hover??this._getOption("hover"),a=t?.direction??this._getOption("direction"),l=t?.speed??this._getOption("speed"),h=t?.defaultTheme??this._getOption("defaultTheme");let d={...this._animationConfig,autoplay:!s&&o,loop:"number"==typeof e||e,renderer:this._worker?"svg":this._animationConfig.renderer??"svg"},[u,p,c]=await Promise.all([this._dotLottieLoader.getTheme(h),this._getLottiePlayerInstance(),this._getAudioFactory()]);u&&this._animation?this._animation=await async function(t,e){let[{relottie:o},{default:n}]=await Promise.all([i.e(305).then(i.bind(i,3686)),i.e(470).then(i.bind(i,9470))]),r=await o().use(n,{lss:e}).process(JSON.stringify(t));return JSON.parse(r.value)}(this._animation,u):this._animation=await this._dotLottieLoader.getAnimation(this._currentAnimationId??""),!this._activeStateId||this._inInteractiveMode?(this.destroy(),this._setPlayerState((()=>({defaultTheme:h,playMode:n,intermission:r,hover:s,loop:e}))),this._lottie=c?p.loadAnimation({...d,container:this._container,animationData:this._animation,audioFactory:c}):p.loadAnimation({...d,container:this._container,animationData:this._animation}),typeof this._lottie.resetSegments>"u"&&(this._lottie.resetSegments=()=>{this._lottie?.playSegments([0,this._lottie.totalFrames],!0)}),this.addEventListeners(),this._container&&(this._container.__lottie=this._lottie),this._setPlayerState((()=>({direction:a,speed:l}))),o&&!s&&this.play(),this._updateTestData()):this.enterInteractiveMode(this._activeStateId)}async _getLottiePlayerInstance(){let t,e=this._animationConfig.renderer??"svg";if(this._worker)return t=await i.e(302).then(i.bind(i,8302)),t.default;switch(e){case"svg":t=this._light?await i.e(834).then(i.bind(i,8453)):await i.e(523).then(i.bind(i,3523));break;case"canvas":t=this._light?await i.e(850).then(i.bind(i,6469)):await i.e(896).then(i.bind(i,9515));break;case"html":t=this._light?await i.e(585).then(i.bind(i,5585)):await i.e(275).then(i.bind(i,6275));break;default:throw new Error(`Invalid renderer: ${e}`)}return t.default}_getActiveAnimationId(){let t=this._dotLottieLoader.manifest;return this._activeAnimationId??t?.activeAnimationId??t?.animations[0]?.id??void 0}async load(t){if("loading"!==this._currentState)try{if(this.setCurrentState("loading"),"string"==typeof this._src)if(function(t){try{return ae(JSON.parse(t))}catch{return!1}}(this._src)){let t=JSON.parse(this._src);this._dotLottieLoader.loadFromLottieJSON(t)}else{let t=new URL(this._src,window.location.href);await this._dotLottieLoader.loadFromUrl(t.toString())}else{if("object"!=typeof this._src||!ae(this._src))throw ne("Invalid src provided");this._dotLottieLoader.loadFromLottieJSON(this._src)}if(!this._dotLottieLoader.manifest)throw ne("No manifest found");let e=this._getActiveAnimationId();if(!e)throw ne("No active animation found");await this._setCurrentAnimation(e),await this.render(t)}catch(t){this.setCurrentState("error"),t instanceof Error&&t.message}}setErrorState(t){this.setCurrentState("error")}_requireValidDirection(t){if(-1!==t&&1!==t)throw ne("Direction can only be -1 (backwards) or 1 (forwards)")}_requireValidIntermission(t){if(t<0||!Number.isInteger(t))throw ne("intermission must be a positive number")}_requireValidLoop(t){if("number"==typeof t&&(!Number.isInteger(t)||t<0))throw ne("loop must be a positive number or boolean")}_requireValidSpeed(t){if("number"!=typeof t)throw ne("speed must be a number")}_requireValidBackground(t){if("string"!=typeof t)throw ne("background must be a string")}_requireValidAutoplay(t){if("boolean"!=typeof t)throw ne("autoplay must be a boolean")}_requireValidPlaybackOptions(t){t.direction&&this._requireValidDirection(t.direction),t.intermission&&this._requireValidIntermission(t.intermission),t.loop&&this._requireValidLoop(t.loop),t.speed&&this._requireValidSpeed(t.speed)}}},5442:(t,e,i)=>{i.r(e),i.d(e,{DotLottiePlayer:()=>_t,PlayMode:()=>lt.g});var o,n=i(9061),r=(i(9375),i(2438),i(2266),i(7327),i(20),i(4840),i(7534),i(4786),i(9033),i(8903),i(1018),i(1415),i(4448),i(8871),i(6539),window),s=r.ShadowRoot&&(void 0===r.ShadyCSS||r.ShadyCSS.nativeShadow)&&"adoptedStyleSheets"in Document.prototype&&"replace"in CSSStyleSheet.prototype,a=Symbol(),l=new WeakMap,h=class{constructor(t,e,i){if(this._$cssResult$=!0,i!==a)throw Error("CSSResult is not constructable. Use `unsafeCSS` or `css` instead.");this.cssText=t,this.t=e}get styleSheet(){let t=this.o,e=this.t;if(s&&void 0===t){let i=void 0!==e&&1===e.length;i&&(t=l.get(e)),void 0===t&&((this.o=t=new CSSStyleSheet).replaceSync(this.cssText),i&&l.set(e,t))}return t}toString(){return this.cssText}},d=s?t=>t:t=>t instanceof CSSStyleSheet?(t=>{let e="";for(let i of t.cssRules)e+=i.cssText;return(t=>new h("string"==typeof t?t:t+"",void 0,a))(e)})(t):t,u=window,p=u.trustedTypes,c=p?p.emptyScript:"",f=u.reactiveElementPolyfillSupport,m={toAttribute(t,e){switch(e){case Boolean:t=t?c:null;break;case Object:case Array:t=null==t?t:JSON.stringify(t)}return t},fromAttribute(t,e){let i=t;switch(e){case Boolean:i=null!==t;break;case Number:i=null===t?null:Number(t);break;case Object:case Array:try{i=JSON.parse(t)}catch{i=null}}return i}},v=(t,e)=>e!==t&&(e==e||t==t),_={attribute:!0,type:String,converter:m,reflect:!1,hasChanged:v},y="finalized",g=class extends HTMLElement{constructor(){super(),this._$Ei=new Map,this.isUpdatePending=!1,this.hasUpdated=!1,this._$El=null,this._$Eu()}static addInitializer(t){var e;this.finalize(),(null!==(e=this.h)&&void 0!==e?e:this.h=[]).push(t)}static get observedAttributes(){this.finalize();let t=[];return this.elementProperties.forEach(((e,i)=>{let o=this._$Ep(i,e);void 0!==o&&(this._$Ev.set(o,i),t.push(o))})),t}static createProperty(t,e=_){if(e.state&&(e.attribute=!1),this.finalize(),this.elementProperties.set(t,e),!e.noAccessor&&!this.prototype.hasOwnProperty(t)){let i="symbol"==typeof t?Symbol():"__"+t,o=this.getPropertyDescriptor(t,i,e);void 0!==o&&Object.defineProperty(this.prototype,t,o)}}static getPropertyDescriptor(t,e,i){return{get(){return this[e]},set(o){let n=this[t];this[e]=o,this.requestUpdate(t,n,i)},configurable:!0,enumerable:!0}}static getPropertyOptions(t){return this.elementProperties.get(t)||_}static finalize(){if(this.hasOwnProperty(y))return!1;this[y]=!0;let t=Object.getPrototypeOf(this);if(t.finalize(),void 0!==t.h&&(this.h=[...t.h]),this.elementProperties=new Map(t.elementProperties),this._$Ev=new Map,this.hasOwnProperty("properties")){let t=this.properties,e=[...Object.getOwnPropertyNames(t),...Object.getOwnPropertySymbols(t)];for(let i of e)this.createProperty(i,t[i])}return this.elementStyles=this.finalizeStyles(this.styles),!0}static finalizeStyles(t){let e=[];if(Array.isArray(t)){let i=new Set(t.flat(1/0).reverse());for(let t of i)e.unshift(d(t))}else void 0!==t&&e.push(d(t));return e}static _$Ep(t,e){let i=e.attribute;return!1===i?void 0:"string"==typeof i?i:"string"==typeof t?t.toLowerCase():void 0}_$Eu(){var t;this._$E_=new Promise((t=>this.enableUpdating=t)),this._$AL=new Map,this._$Eg(),this.requestUpdate(),null===(t=this.constructor.h)||void 0===t||t.forEach((t=>t(this)))}addController(t){var e,i;(null!==(e=this._$ES)&&void 0!==e?e:this._$ES=[]).push(t),void 0!==this.renderRoot&&this.isConnected&&(null===(i=t.hostConnected)||void 0===i||i.call(t))}removeController(t){var e;null===(e=this._$ES)||void 0===e||e.splice(this._$ES.indexOf(t)>>>0,1)}_$Eg(){this.constructor.elementProperties.forEach(((t,e)=>{this.hasOwnProperty(e)&&(this._$Ei.set(e,this[e]),delete this[e])}))}createRenderRoot(){var t;let e=null!==(t=this.shadowRoot)&&void 0!==t?t:this.attachShadow(this.constructor.shadowRootOptions);return((t,e)=>{s?t.adoptedStyleSheets=e.map((t=>t instanceof CSSStyleSheet?t:t.styleSheet)):e.forEach((e=>{let i=document.createElement("style"),o=r.litNonce;void 0!==o&&i.setAttribute("nonce",o),i.textContent=e.cssText,t.appendChild(i)}))})(e,this.constructor.elementStyles),e}connectedCallback(){var t;void 0===this.renderRoot&&(this.renderRoot=this.createRenderRoot()),this.enableUpdating(!0),null===(t=this._$ES)||void 0===t||t.forEach((t=>{var e;return null===(e=t.hostConnected)||void 0===e?void 0:e.call(t)}))}enableUpdating(t){}disconnectedCallback(){var t;null===(t=this._$ES)||void 0===t||t.forEach((t=>{var e;return null===(e=t.hostDisconnected)||void 0===e?void 0:e.call(t)}))}attributeChangedCallback(t,e,i){this._$AK(t,i)}_$EO(t,e,i=_){var o;let n=this.constructor._$Ep(t,i);if(void 0!==n&&!0===i.reflect){let r=(void 0!==(null===(o=i.converter)||void 0===o?void 0:o.toAttribute)?i.converter:m).toAttribute(e,i.type);this._$El=t,null==r?this.removeAttribute(n):this.setAttribute(n,r),this._$El=null}}_$AK(t,e){var i;let o=this.constructor,n=o._$Ev.get(t);if(void 0!==n&&this._$El!==n){let t=o.getPropertyOptions(n),r="function"==typeof t.converter?{fromAttribute:t.converter}:void 0!==(null===(i=t.converter)||void 0===i?void 0:i.fromAttribute)?t.converter:m;this._$El=n,this[n]=r.fromAttribute(e,t.type),this._$El=null}}requestUpdate(t,e,i){let o=!0;void 0!==t&&(((i=i||this.constructor.getPropertyOptions(t)).hasChanged||v)(this[t],e)?(this._$AL.has(t)||this._$AL.set(t,e),!0===i.reflect&&this._$El!==t&&(void 0===this._$EC&&(this._$EC=new Map),this._$EC.set(t,i))):o=!1),!this.isUpdatePending&&o&&(this._$E_=this._$Ej())}async _$Ej(){this.isUpdatePending=!0;try{await this._$E_}catch(t){Promise.reject(t)}let t=this.scheduleUpdate();return null!=t&&await t,!this.isUpdatePending}scheduleUpdate(){return this.performUpdate()}performUpdate(){var t;if(!this.isUpdatePending)return;this.hasUpdated,this._$Ei&&(this._$Ei.forEach(((t,e)=>this[e]=t)),this._$Ei=void 0);let e=!1,i=this._$AL;try{e=this.shouldUpdate(i),e?(this.willUpdate(i),null===(t=this._$ES)||void 0===t||t.forEach((t=>{var e;return null===(e=t.hostUpdate)||void 0===e?void 0:e.call(t)})),this.update(i)):this._$Ek()}catch(t){throw e=!1,this._$Ek(),t}e&&this._$AE(i)}willUpdate(t){}_$AE(t){var e;null===(e=this._$ES)||void 0===e||e.forEach((t=>{var e;return null===(e=t.hostUpdated)||void 0===e?void 0:e.call(t)})),this.hasUpdated||(this.hasUpdated=!0,this.firstUpdated(t)),this.updated(t)}_$Ek(){this._$AL=new Map,this.isUpdatePending=!1}get updateComplete(){return this.getUpdateComplete()}getUpdateComplete(){return this._$E_}shouldUpdate(t){return!0}update(t){void 0!==this._$EC&&(this._$EC.forEach(((t,e)=>this._$EO(e,this[e],t))),this._$EC=void 0),this._$Ek()}updated(t){}firstUpdated(t){}};g[y]=!0,g.elementProperties=new Map,g.elementStyles=[],g.shadowRootOptions={mode:"open"},f?.({ReactiveElement:g}),(null!==(o=u.reactiveElementVersions)&&void 0!==o?o:u.reactiveElementVersions=[]).push("1.6.3");var b,w=window,A=w.trustedTypes,C=A?A.createPolicy("lit-html",{createHTML:t=>t}):void 0,L="$lit$",S=`lit$${(Math.random()+"").slice(9)}$`,k="?"+S,x=`<${k}>`,$=document,O=()=>$.createComment(""),P=t=>null===t||"object"!=typeof t&&"function"!=typeof t,I=Array.isArray,M="[ \t\n\f\r]",E=/<(?:(!--|\/[^a-zA-Z])|(\/?[a-zA-Z][^>\s]*)|(\/?$))/g,T=/-->/g,U=/>/g,j=RegExp(`>|${M}(?:([^\\s"'>=/]+)(${M}*=${M}*(?:[^ \t\n\f\r"'\`<>=]|("|')|))|$)`,"g"),N=/'/g,z=/"/g,D=/^(?:script|style|textarea|title)$/i,F=(t=>(e,...i)=>({_$litType$:t,strings:e,values:i}))(1),V=Symbol.for("lit-noChange"),B=Symbol.for("lit-nothing"),H=new WeakMap,q=$.createTreeWalker($,129,null,!1);function R(t,e){if(!Array.isArray(t)||!t.hasOwnProperty("raw"))throw Error("invalid template strings array");return void 0!==C?C.createHTML(e):e}var Z=class{constructor({strings:t,_$litType$:e},i){let o;this.parts=[];let n=0,r=0,s=t.length-1,a=this.parts,[l,h]=((t,e)=>{let i,o=t.length-1,n=[],r=2===e?"<svg>":"",s=E;for(let e=0;e<o;e++){let o,a,l=t[e],h=-1,d=0;for(;d<l.length&&(s.lastIndex=d,a=s.exec(l),null!==a);)d=s.lastIndex,s===E?"!--"===a[1]?s=T:void 0!==a[1]?s=U:void 0!==a[2]?(D.test(a[2])&&(i=RegExp("</"+a[2],"g")),s=j):void 0!==a[3]&&(s=j):s===j?">"===a[0]?(s=i??E,h=-1):void 0===a[1]?h=-2:(h=s.lastIndex-a[2].length,o=a[1],s=void 0===a[3]?j:'"'===a[3]?z:N):s===z||s===N?s=j:s===T||s===U?s=E:(s=j,i=void 0);let u=s===j&&t[e+1].startsWith("/>")?" ":"";r+=s===E?l+x:h>=0?(n.push(o),l.slice(0,h)+L+l.slice(h)+S+u):l+S+(-2===h?(n.push(void 0),e):u)}return[R(t,r+(t[o]||"<?>")+(2===e?"</svg>":"")),n]})(t,e);if(this.el=Z.createElement(l,i),q.currentNode=this.el.content,2===e){let t=this.el.content,e=t.firstChild;e.remove(),t.append(...e.childNodes)}for(;null!==(o=q.nextNode())&&a.length<s;){if(1===o.nodeType){if(o.hasAttributes()){let t=[];for(let e of o.getAttributeNames())if(e.endsWith(L)||e.startsWith(S)){let i=h[r++];if(t.push(e),void 0!==i){let t=o.getAttribute(i.toLowerCase()+L).split(S),e=/([.?@])?(.*)/.exec(i);a.push({type:1,index:n,name:e[2],strings:t,ctor:"."===e[1]?X:"?"===e[1]?Y:"@"===e[1]?tt:G})}else a.push({type:6,index:n})}for(let e of t)o.removeAttribute(e)}if(D.test(o.tagName)){let t=o.textContent.split(S),e=t.length-1;if(e>0){o.textContent=A?A.emptyScript:"";for(let i=0;i<e;i++)o.append(t[i],O()),q.nextNode(),a.push({type:2,index:++n});o.append(t[e],O())}}}else if(8===o.nodeType)if(o.data===k)a.push({type:2,index:n});else{let t=-1;for(;-1!==(t=o.data.indexOf(S,t+1));)a.push({type:7,index:n}),t+=S.length-1}n++}}static createElement(t,e){let i=$.createElement("template");return i.innerHTML=t,i}};function J(t,e,i=t,o){var n,r,s,a;if(e===V)return e;let l=void 0!==o?null===(n=i._$Co)||void 0===n?void 0:n[o]:i._$Cl,h=P(e)?void 0:e._$litDirective$;return l?.constructor!==h&&(null===(r=l?._$AO)||void 0===r||r.call(l,!1),void 0===h?l=void 0:(l=new h(t),l._$AT(t,i,o)),void 0!==o?(null!==(s=(a=i)._$Co)&&void 0!==s?s:a._$Co=[])[o]=l:i._$Cl=l),void 0!==l&&(e=J(t,l._$AS(t,e.values),l,o)),e}var W=class{constructor(t,e){this._$AV=[],this._$AN=void 0,this._$AD=t,this._$AM=e}get parentNode(){return this._$AM.parentNode}get _$AU(){return this._$AM._$AU}u(t){var e;let{el:{content:i},parts:o}=this._$AD,n=(null!==(e=t?.creationScope)&&void 0!==e?e:$).importNode(i,!0);q.currentNode=n;let r=q.nextNode(),s=0,a=0,l=o[0];for(;void 0!==l;){if(s===l.index){let e;2===l.type?e=new K(r,r.nextSibling,this,t):1===l.type?e=new l.ctor(r,l.name,l.strings,this,t):6===l.type&&(e=new et(r,this,t)),this._$AV.push(e),l=o[++a]}s!==l?.index&&(r=q.nextNode(),s++)}return q.currentNode=$,n}v(t){let e=0;for(let i of this._$AV)void 0!==i&&(void 0!==i.strings?(i._$AI(t,i,e),e+=i.strings.length-2):i._$AI(t[e])),e++}},K=class{constructor(t,e,i,o){var n;this.type=2,this._$AH=B,this._$AN=void 0,this._$AA=t,this._$AB=e,this._$AM=i,this.options=o,this._$Cp=null===(n=o?.isConnected)||void 0===n||n}get _$AU(){var t,e;return null!==(e=null===(t=this._$AM)||void 0===t?void 0:t._$AU)&&void 0!==e?e:this._$Cp}get parentNode(){let t=this._$AA.parentNode,e=this._$AM;return void 0!==e&&11===t?.nodeType&&(t=e.parentNode),t}get startNode(){return this._$AA}get endNode(){return this._$AB}_$AI(t,e=this){t=J(this,t,e),P(t)?t===B||null==t||""===t?(this._$AH!==B&&this._$AR(),this._$AH=B):t!==this._$AH&&t!==V&&this._(t):void 0!==t._$litType$?this.g(t):void 0!==t.nodeType?this.$(t):(t=>I(t)||"function"==typeof t?.[Symbol.iterator])(t)?this.T(t):this._(t)}k(t){return this._$AA.parentNode.insertBefore(t,this._$AB)}$(t){this._$AH!==t&&(this._$AR(),this._$AH=this.k(t))}_(t){this._$AH!==B&&P(this._$AH)?this._$AA.nextSibling.data=t:this.$($.createTextNode(t)),this._$AH=t}g(t){var e;let{values:i,_$litType$:o}=t,n="number"==typeof o?this._$AC(t):(void 0===o.el&&(o.el=Z.createElement(R(o.h,o.h[0]),this.options)),o);if((null===(e=this._$AH)||void 0===e?void 0:e._$AD)===n)this._$AH.v(i);else{let t=new W(n,this),e=t.u(this.options);t.v(i),this.$(e),this._$AH=t}}_$AC(t){let e=H.get(t.strings);return void 0===e&&H.set(t.strings,e=new Z(t)),e}T(t){I(this._$AH)||(this._$AH=[],this._$AR());let e,i=this._$AH,o=0;for(let n of t)o===i.length?i.push(e=new K(this.k(O()),this.k(O()),this,this.options)):e=i[o],e._$AI(n),o++;o<i.length&&(this._$AR(e&&e._$AB.nextSibling,o),i.length=o)}_$AR(t=this._$AA.nextSibling,e){var i;for(null===(i=this._$AP)||void 0===i||i.call(this,!1,!0,e);t&&t!==this._$AB;){let e=t.nextSibling;t.remove(),t=e}}setConnected(t){var e;void 0===this._$AM&&(this._$Cp=t,null===(e=this._$AP)||void 0===e||e.call(this,t))}},G=class{constructor(t,e,i,o,n){this.type=1,this._$AH=B,this._$AN=void 0,this.element=t,this.name=e,this._$AM=o,this.options=n,i.length>2||""!==i[0]||""!==i[1]?(this._$AH=Array(i.length-1).fill(new String),this.strings=i):this._$AH=B}get tagName(){return this.element.tagName}get _$AU(){return this._$AM._$AU}_$AI(t,e=this,i,o){let n=this.strings,r=!1;if(void 0===n)t=J(this,t,e,0),r=!P(t)||t!==this._$AH&&t!==V,r&&(this._$AH=t);else{let o,s,a=t;for(t=n[0],o=0;o<n.length-1;o++)s=J(this,a[i+o],e,o),s===V&&(s=this._$AH[o]),r||(r=!P(s)||s!==this._$AH[o]),s===B?t=B:t!==B&&(t+=(s??"")+n[o+1]),this._$AH[o]=s}r&&!o&&this.j(t)}j(t){t===B?this.element.removeAttribute(this.name):this.element.setAttribute(this.name,t??"")}},X=class extends G{constructor(){super(...arguments),this.type=3}j(t){this.element[this.name]=t===B?void 0:t}},Q=A?A.emptyScript:"",Y=class extends G{constructor(){super(...arguments),this.type=4}j(t){t&&t!==B?this.element.setAttribute(this.name,Q):this.element.removeAttribute(this.name)}},tt=class extends G{constructor(t,e,i,o,n){super(t,e,i,o,n),this.type=5}_$AI(t,e=this){var i;if((t=null!==(i=J(this,t,e,0))&&void 0!==i?i:B)===V)return;let o=this._$AH,n=t===B&&o!==B||t.capture!==o.capture||t.once!==o.once||t.passive!==o.passive,r=t!==B&&(o===B||n);n&&this.element.removeEventListener(this.name,this,o),r&&this.element.addEventListener(this.name,this,t),this._$AH=t}handleEvent(t){var e,i;"function"==typeof this._$AH?this._$AH.call(null!==(i=null===(e=this.options)||void 0===e?void 0:e.host)&&void 0!==i?i:this.element,t):this._$AH.handleEvent(t)}},et=class{constructor(t,e,i){this.element=t,this.type=6,this._$AN=void 0,this._$AM=e,this.options=i}get _$AU(){return this._$AM._$AU}_$AI(t){J(this,t)}},it=w.litHtmlPolyfillSupport;it?.(Z,K),(null!==(b=w.litHtmlVersions)&&void 0!==b?b:w.litHtmlVersions=[]).push("2.8.0");var ot,nt,rt=class extends g{constructor(){super(...arguments),this.renderOptions={host:this},this._$Do=void 0}createRenderRoot(){var t,e;let i=super.createRenderRoot();return null!==(t=(e=this.renderOptions).renderBefore)&&void 0!==t||(e.renderBefore=i.firstChild),i}update(t){let e=this.render();this.hasUpdated||(this.renderOptions.isConnected=this.isConnected),super.update(t),this._$Do=((t,e,i)=>{var o,n;let r=null!==(o=i?.renderBefore)&&void 0!==o?o:e,s=r._$litPart$;if(void 0===s){let t=null!==(n=i?.renderBefore)&&void 0!==n?n:null;r._$litPart$=s=new K(e.insertBefore(O(),t),t,void 0,i??{})}return s._$AI(t),s})(e,this.renderRoot,this.renderOptions)}connectedCallback(){var t;super.connectedCallback(),null===(t=this._$Do)||void 0===t||t.setConnected(!0)}disconnectedCallback(){var t;super.disconnectedCallback(),null===(t=this._$Do)||void 0===t||t.setConnected(!1)}render(){return V}};rt.finalized=!0,rt._$litElement$=!0,null===(ot=globalThis.litElementHydrateSupport)||void 0===ot||ot.call(globalThis,{LitElement:rt});var st=globalThis.litElementPolyfillSupport;st?.({LitElement:rt}),(null!==(nt=globalThis.litElementVersions)&&void 0!==nt?nt:globalThis.litElementVersions=[]).push("3.3.3");var at=((t,...e)=>{let i=1===t.length?t[0]:e.reduce(((e,i,o)=>e+(t=>{if(!0===t._$cssResult$)return t.cssText;if("number"==typeof t)return t;throw Error("Value passed to 'css' function must be a 'css' function result: "+t+". Use 'unsafeCSS' to pass non-literal values, but take care to ensure page security.")})(i)+t[o+1]),t[0]);return new h(i,t,a)})`
  @font-face {
    font-family: 'Karla';
    font-weight: regular;
    src: url('./fonts/Karla-regular.woff') format('woff');
  }

  * {
    box-sizing: border-box;
  }

  :host {
    --lottie-player-toolbar-height: 35px;
    --lottie-player-toolbar-background-color: transparent;
    --lottie-player-toolbar-hover-background-color: #f3f6f8;
    --lottie-player-toolbar-icon-color: #20272c;
    --lottie-player-toolbar-icon-hover-color: #f3f6f8;
    --lottie-player-toolbar-icon-active-color: #00ddb3;
    --lottie-player-seeker-track-color: #00ddb3;
    --lottie-player-seeker-accent-color: #00c1a2;
    --lottie-player-seeker-thumb-color: #00c1a2;
    --lottie-player-options-separator: #d9e0e6;

    display: block;
    width: 100%;
    height: 100%;

    font-family: 'Karla', sans-serif;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  .active {
    color: var(--lottie-player-toolbar-icon-active-color) !important;
  }

  .main {
    position: relative;
    display: flex;
    flex-direction: column;
    height: 100%;
    width: 100%;
  }

  .animation {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
  }
  .animation.controls {
    height: calc(100% - var(--lottie-player-toolbar-height));
  }

  .toolbar {
    display: flex;
    align-items: center;
    justify-items: center;
    background-color: var(--lottie-player-toolbar-background-color);
    margin: 0 8px;
    height: var(--lottie-player-toolbar-height);
  }

  .btn-spacing-left {
    margin-right: 4px;
    margin-left: 8px;
  }

  .btn-spacing-center {
    margin-right: 4px;
    margin-left: 4px;
  }

  .btn-spacing-right {
    margin-right: 8px;
    margin-left: 4px;
  }

  .toolbar button {
    color: #20272c;
    cursor: pointer;
    fill: var(--lottie-player-toolbar-icon-color);
    display: flex;
    background: none;
    border: 0px;
    border-radius: 4px;
    padding: 4px;
    outline: none;
    width: 24px;
    height: 24px;
    align-items: center;
  }

  .toolbar button:hover {
    background-color: var(--lottie-player-toolbar-icon-hover-color);
    border-style: solid;
    border-radius: 2px;
  }

  .toolbar button.active {
    fill: var(--lottie-player-toolbar-icon-active-color);
  }

  .toolbar button.active:hover {
    fill: var(--lottie-player-toolbar-icon-hover-color);
    border-radius: 4px;
  }

  .toolbar button:focus-visible {
    outline: 2px solid var(--lottie-player-toolbar-icon-active-color);
    border-radius: 4px;
    box-sizing: border-box;
  }

  .toolbar button svg {
    width: 16px;
    height: 16px;
  }

  .toolbar button.disabled svg {
    display: none;
  }

  .popover {
    position: absolute;
    bottom: 40px;
    left: calc(100% - 239px);
    width: 224px;
    min-height: 84px;
    max-height: 300px;
    background-color: #ffffff;
    box-shadow: 0px 8px 48px 0px rgba(243, 246, 248, 0.15), 0px 8px 16px 0px rgba(61, 72, 83, 0.16),
      0px 0px 1px 0px rgba(61, 72, 83, 0.36);
    border-radius: 8px;
    padding: 8px;
    z-index: 100;
    overflow-y: scroll;
    scrollbar-width: none;
  }
  .popover:focus-visible {
    outline: 2px solid var(--lottie-player-toolbar-icon-active-color);
    border-radius: 4px;
    box-sizing: border-box;
  }

  .popover::-webkit-scrollbar {
    width: 0px;
  }

  .popover-button {
    background: none;
    border: none;
    font-family: inherit;
    width: 100%;
    flex-direction: row;
    cursor: pointer;
    height: 32px;
    color: #20272c;
    justify-content: space-between;
    display: flex;
    padding: 4px 8px;
    align-items: flex-start;
    gap: 8px;
    align-self: stretch;
    border-radius: 4px;
  }

  .popover-button:focus-visible {
    outline: 2px solid var(--lottie-player-toolbar-icon-active-color);
    border-radius: 4px;
    box-sizing: border-box;
  }

  .popover-button:hover {
    background-color: var(--lottie-player-toolbar-hover-background-color);
  }

  .popover-button-text {
    display: flex;
    color: #20272c;
    flex-direction: column;
    align-self: stretch;
    justify-content: center;
    font-family: inherit;
    font-size: 14px;
    font-style: normal;
    font-weight: 400;
    line-height: 150%;
    letter-spacing: -0.28px;
  }

  .reset-btn {
    font-size: 12px;
    cursor: pointer;
    font-family: inherit;
    background: none;
    border: none;
    font-weight: 400;
    line-height: 18px;
    letter-spacing: 0em;
    text-align: left;
    color: #63727e;
    padding: 0;
    width: 31px;
    height: 18px;
  }
  .reset-btn:focus-visible {
    outline: 2px solid var(--lottie-player-toolbar-icon-active-color);
    border-radius: 4px;
    box-sizing: border-box;
  }
  .reset-btn:hover {
    color: #20272c;
  }

  .option-title-button {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 32px;
    align-items: center;
    gap: 4px;
    align-self: stretch;
    cursor: pointer;
    color: var(--lottie-player-toolbar-icon-color);
    border: none;
    background: none;
    padding: 4px;
    font-family: inherit;
    font-size: 16px;
    font-weight: 700;
    line-height: 150%;
    letter-spacing: -0.32px;
  }
  .option-title-button.themes {
    width: auto;
    padding: 0;
  }
  .option-title-button:hover {
    background-color: var(--lottie-player-toolbar-icon-hover-color);
  }

  .option-title-themes-row {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1 0 0;
  }
  .option-title-themes-row:hover {
    background-color: var(--lottie-player-toolbar-icon-hover-color);
  }

  .option-title-button:focus-visible {
    outline: 2px solid var(--lottie-player-toolbar-icon-active-color);
    border-radius: 4px;
    box-sizing: border-box;
  }

  .option-title-text {
    font-size: 16px;
    font-style: normal;
    font-weight: 700;
    line-height: 150%;
    letter-spacing: -0.32px;
  }

  .option-title-separator {
    margin: 8px -8px;
    border-bottom: 1px solid var(--lottie-player-options-separator);
  }

  .option-title-chevron {
    display: flex;
    padding: 4px;
    border-radius: 8px;
    justify-content: center;
    align-items: center;
    gap: 8px;
  }

  .option-row {
    display: flex;
    flex-direction: column;
  }
  .option-row > ul {
    padding: 0;
    margin: 0;
  }

  .option-button {
    width: 100%;
    background: none;
    border: none;
    font-family: inherit;
    display: flex;
    padding: 4px 8px;
    color: #20272c;
    overflow: hidden;
    align-items: center;
    gap: 8px;
    align-self: stretch;
    cursor: pointer;
    height: 32px;
    font-family: inherit;
    font-size: 14px;
    border-radius: 4px;
  }
  .option-button:hover {
    background-color: var(--lottie-player-toolbar-hover-background-color);
  }
  .option-button:focus-visible {
    outline: 2px solid var(--lottie-player-toolbar-icon-active-color);
    border-radius: 4px;
    box-sizing: border-box;
  }

  .option-tick {
    display: flex;
    width: 24px;
    height: 24px;
    align-items: flex-start;
    gap: 8px;
  }

  .seeker {
    height: 4px;
    width: 95%;
    outline: none;
    -webkit-appearance: none;
    -moz-apperance: none;
    border-radius: 9999px;
    cursor: pointer;
    background-image: linear-gradient(
      to right,
      rgb(0, 221, 179) calc(var(--seeker) * 1%),
      rgb(217, 224, 230) calc(var(--seeker) * 1%)
    );
  }
  .seeker.to-left {
    background-image: linear-gradient(
      to right,
      rgb(217, 224, 230) calc(var(--seeker) * 1%),
      rgb(0, 221, 179) calc(var(--seeker) * 1%)
    );
  }
  .seeker::-webkit-slider-runnable-track:focus-visible {
    color: #f07167;
    accent-color: #00ddb3;
  }

  .seeker::-webkit-slider-runnable-track {
    width: 100%;
    height: 5px;
    cursor: pointer;
  }
  .seeker::-webkit-slider-thumb {
    -webkit-appearance: none;
    height: 16px;
    width: 16px;
    border-radius: 50%;
    background: var(--lottie-player-seeker-thumb-color);
    cursor: pointer;
    margin-top: -5px;
  }
  .seeker:focus-visible::-webkit-slider-thumb {
    background: var(--lottie-player-seeker-thumb-color);
    outline: 2px solid var(--lottie-player-seeker-track-color);
    border: 1.5px solid #ffffff;
  }
  .seeker::-webkit-slider-thumb:hover {
    background: #019d91;
  }
  .seeker::-moz-range-thumb {
    appearance: none;
    height: 16px;
    width: 16px;
    border-radius: 50%;
    background: var(--lottie-player-seeker-thumb-color);
    cursor: pointer;
    margin-top: -5px;
    border-color: transparent;
  }
  .seeker:focus-visible::-moz-range-thumb {
    background: var(--lottie-player-seeker-thumb-color);
    outline: 2px solid var(--lottie-player-seeker-track-color);
    border: 1.5px solid #ffffff;
  }

  #animation-container[lottietype="btnIcon"] path[fill] {
    fill: var(--lottie-player-path-fill) !important;
  }
  #animation-container[lottietype="btnIcon"] path[stroke] {
    stroke: var(--lottie-player-path-stroke) !important;
  }

  .error {
    display: flex;
    justify-content: center;
    margin: auto;
    height: 100%;
    align-items: center;
  }
`,lt=i(4356),ht=(i(3084),i(2719)),dt=(t,e)=>"method"===e.kind&&e.descriptor&&!("value"in e.descriptor)?{...e,finisher(i){i.createProperty(e.key,t)}}:{kind:"field",key:Symbol(),placement:"own",descriptor:{},originalKey:e.key,initializer(){"function"==typeof e.initializer&&(this[e.key]=e.initializer.call(this))},finisher(i){i.createProperty(e.key,t)}};function ut(t){return(e,i)=>void 0!==i?((t,e,i)=>{e.constructor.createProperty(i,t)})(t,e,i):dt(t,e)}var pt;null===(pt=window.HTMLSlotElement)||void 0===pt||pt.prototype.assignedElements;var ct,ft,mt="2.7.0",vt="dotlottie-player",_t=class extends rt{constructor(...t){super(...t),(0,n.A)(this,"defaultTheme",""),(0,n.A)(this,"container",void 0),(0,n.A)(this,"playMode",lt.g.Normal),(0,n.A)(this,"autoplay",!1),(0,n.A)(this,"background","transparent"),(0,n.A)(this,"lottietype","animation"),(0,n.A)(this,"controls",!1),(0,n.A)(this,"direction",1),(0,n.A)(this,"hover",!1),(0,n.A)(this,"loop",void 0),(0,n.A)(this,"renderer","svg"),(0,n.A)(this,"speed",1),(0,n.A)(this,"src",void 0),(0,n.A)(this,"intermission",0),(0,n.A)(this,"activeAnimationId",null),(0,n.A)(this,"light",!1),(0,n.A)(this,"worker",!1),(0,n.A)(this,"activeStateId",void 0),(0,n.A)(this,"_seeker",0),(0,n.A)(this,"_dotLottieCommonPlayer",void 0),(0,n.A)(this,"_io",void 0),(0,n.A)(this,"_loop",void 0),(0,n.A)(this,"_renderer","svg"),(0,n.A)(this,"_unsubscribeListeners",void 0),(0,n.A)(this,"_hasMultipleAnimations",!1),(0,n.A)(this,"_hasMultipleThemes",!1),(0,n.A)(this,"_hasMultipleStates",!1),(0,n.A)(this,"_popoverIsOpen",!1),(0,n.A)(this,"_animationsTabIsOpen",!1),(0,n.A)(this,"_statesTabIsOpen",!1),(0,n.A)(this,"_styleTabIsOpen",!1),(0,n.A)(this,"_themesForCurrentAnimation",[]),(0,n.A)(this,"_statesForCurrentAnimation",[])}_parseLoop(t){let e=parseInt(t,10);return Number.isInteger(e)&&e>0?(this._loop=e,e):"string"==typeof t&&["true","false"].includes(t)?(this._loop="true"===t,this._loop):((0,lt.c)("loop must be a positive integer or a boolean"),!1)}_handleSeekChange(t){let e=t.currentTarget;try{let t=parseInt(e.value,10);if(!this._dotLottieCommonPlayer)return;let i=t/100*this._dotLottieCommonPlayer.totalFrames;this.seek(i)}catch{throw(0,lt.a)("Error while seeking animation")}}_initListeners(){let t=this._dotLottieCommonPlayer;void 0!==t?(this._unsubscribeListeners=t.state.subscribe(((t,e)=>{this._seeker=t.seeker,this.requestUpdate(),e.currentState!==t.currentState&&this.dispatchEvent(new CustomEvent(t.currentState)),this.dispatchEvent(new CustomEvent(lt.e.Frame,{detail:{frame:t.frame,seeker:t.seeker}})),this.dispatchEvent(new CustomEvent(lt.e.VisibilityChange,{detail:{visibilityPercentage:t.visibilityPercentage}}))})),t.addEventListener("complete",(()=>{this.dispatchEvent(new CustomEvent(lt.e.Complete))})),t.addEventListener("loopComplete",(()=>{this.dispatchEvent(new CustomEvent(lt.e.LoopComplete))})),t.addEventListener("DOMLoaded",(()=>{let t=this.getManifest();t&&t.themes&&(this._themesForCurrentAnimation=t.themes.filter((t=>t.animations.includes(this.getCurrentAnimationId()||"")))),t&&t.states&&(this._hasMultipleStates=t.states.length>0,this._statesForCurrentAnimation=[],t.states.forEach((t=>{this._statesForCurrentAnimation.push(t)}))),this.dispatchEvent(new CustomEvent(lt.e.Ready))})),t.addEventListener("data_ready",(()=>{this.dispatchEvent(new CustomEvent(lt.e.DataReady))})),t.addEventListener("data_failed",(()=>{this.dispatchEvent(new CustomEvent(lt.e.DataFail))})),window&&window.addEventListener("click",(t=>this._clickOutListener(t)))):(0,lt.c)("player not initialized - cannot add event listeners","dotlottie-player-component")}async load(t,e,i){if(!this.shadowRoot)return;this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.destroy(),this._dotLottieCommonPlayer=new lt.j(t,this.container,{rendererSettings:e??{scaleMode:"noScale",clearCanvas:!0,progressiveLoad:!0,hideOnTransparent:!0},hover:this.hasAttribute("hover")?this.hover:void 0,renderer:this.hasAttribute("renderer")?this._renderer:void 0,loop:this.hasAttribute("loop")?this._loop:void 0,direction:this.hasAttribute("direction")?1===this.direction?1:-1:void 0,speed:this.hasAttribute("speed")?this.speed:void 0,intermission:this.hasAttribute("intermission")?Number(this.intermission):void 0,playMode:this.hasAttribute("playMode")?this.playMode:void 0,autoplay:this.hasAttribute("autoplay")?this.autoplay:void 0,activeAnimationId:this.hasAttribute("activeAnimationId")?this.activeAnimationId:void 0,defaultTheme:this.hasAttribute("default-theme")?this.defaultTheme:void 0,light:this.light,worker:this.worker,activeStateId:this.hasAttribute("activeStateId")?this.activeStateId:void 0}),await this._dotLottieCommonPlayer.load(i);let o=this.getManifest();this._hasMultipleAnimations=this.animationCount()>1,o&&(o.themes&&(this._themesForCurrentAnimation=o.themes.filter((t=>t.animations.includes(this.getCurrentAnimationId()||""))),this._hasMultipleThemes=o.themes.length>0),o.states&&(this._hasMultipleStates=o.states.length>0,this._statesForCurrentAnimation=[],o.states.forEach((t=>{this._statesForCurrentAnimation.push(t)})))),this._initListeners()}getCurrentAnimationId(){return this._dotLottieCommonPlayer?.currentAnimationId}animationCount(){return this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.getManifest()?.animations.length||0}animations(){return this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.getManifest()?.animations.map((t=>t.id))||[]}currentAnimation(){return this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.currentAnimationId?this._dotLottieCommonPlayer.currentAnimationId:""}getState(){return this._dotLottieCommonPlayer?this._dotLottieCommonPlayer.getState():lt.i}getManifest(){return this._dotLottieCommonPlayer?.getManifest()}getLottie(){return this._dotLottieCommonPlayer?.getAnimationInstance()}getVersions(){return{lottieWebVersion:lt.j.getLottieWebVersion(),dotLottiePlayerVersion:`${mt}`}}previous(t){this._dotLottieCommonPlayer?.previous(t)}next(t){this._dotLottieCommonPlayer?.next(t)}reset(){this._dotLottieCommonPlayer?.reset()}play(t,e){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.play(t,e)}pause(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.pause()}stop(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.stop()}playOnShow(t){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.playOnShow(t)}stopPlayOnShow(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.stopPlayOnShow()}playOnScroll(t){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.playOnScroll(t)}stopPlayOnScroll(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.stopPlayOnScroll()}seek(t){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.seek(t)}snapshot(t=!0){if(!this.shadowRoot)return"";let e=this.shadowRoot.querySelector(".animation svg"),i=(new XMLSerializer).serializeToString(e);if(t){let t=document.createElement("a");t.href=`data:image/svg+xml;charset=utf-8,${encodeURIComponent(i)}`,t.download=`download_${this._seeker}.svg`,document.body.appendChild(t),t.click(),document.body.removeChild(t)}return i}setTheme(t){this._dotLottieCommonPlayer?.setDefaultTheme(t)}themes(){return this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.getManifest()?.themes?.map((t=>t.id))||[]}getDefaultTheme(){return this._dotLottieCommonPlayer?this._dotLottieCommonPlayer.defaultTheme:""}getActiveStateMachine(){return this._dotLottieCommonPlayer?this._dotLottieCommonPlayer.activeStateId:""}_freeze(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.freeze()}setSpeed(t=1){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.setSpeed(t)}setDirection(t){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.setDirection(t)}setLooping(t){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.setLoop(t)}isLooping(){return!!this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.loop}togglePlay(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.togglePlay()}toggleLooping(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.toggleLoop()}setPlayMode(t){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.setMode(t)}enterInteractiveMode(t){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.enterInteractiveMode(t)}exitInteractiveMode(){this._dotLottieCommonPlayer&&this._dotLottieCommonPlayer.exitInteractiveMode()}revertToManifestValues(t){this._dotLottieCommonPlayer?.revertToManifestValues(t)}static get styles(){return at}async firstUpdated(){this.container=this.shadowRoot?.querySelector("#animation"),"IntersectionObserver"in window&&(this._io=new IntersectionObserver((t=>{void 0!==t[0]&&t[0].isIntersecting?this._dotLottieCommonPlayer?.currentState===lt.f.Frozen&&this.play():this._dotLottieCommonPlayer?.currentState===lt.f.Playing&&this._freeze()})),this._io.observe(this.container)),this.loop?this._parseLoop(this.loop):this.hasAttribute("loop")&&this._parseLoop("true"),"svg"===this.renderer?this._renderer="svg":"canvas"===this.renderer?this._renderer="canvas":"html"===this.renderer&&(this._renderer="html"),this.src&&await this.load(this.src)}disconnectedCallback(){this._io&&(this._io.disconnect(),this._io=void 0),this._dotLottieCommonPlayer?.destroy(),this._unsubscribeListeners?.(),window&&window.removeEventListener("click",(t=>this._clickOutListener(t)))}_clickOutListener(t){!t.composedPath().some((t=>t instanceof HTMLElement&&(t.classList.contains("popover")||"lottie-animation-options"===t.id)))&&this._popoverIsOpen&&(this._popoverIsOpen=!1,this.requestUpdate())}renderControls(){let t=this._dotLottieCommonPlayer?.currentState===lt.f.Playing,e=this._dotLottieCommonPlayer?.currentState===lt.f.Paused;return F`
      <div id="lottie-controls" aria-label="lottie-animation-controls" class="toolbar">
        ${this._hasMultipleAnimations?F`
              <button @click=${()=>this.previous()} aria-label="Previous animation" class="btn-spacing-left">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    fill-rule="evenodd"
                    clip-rule="evenodd"
                    d="M1.69214 13.5C1.69214 13.7761 1.916 14 2.19214 14C2.46828 14 2.69214 13.7761 2.69214 13.5L2.69214 2.5C2.69214 2.22386 2.46828 2 2.19214 2C1.916 2 1.69214 2.22386 1.69214 2.5V13.5ZM12.5192 13.7828C13.1859 14.174 14.0254 13.6933 14.0254 12.9204L14.0254 3.0799C14.0254 2.30692 13.1859 1.8262 12.5192 2.21747L4.13612 7.13769C3.47769 7.52414 3.47769 8.4761 4.13612 8.86255L12.5192 13.7828Z"
                    fill="#20272C"
                  />
                </svg>
              </button>
            `:F``}
        <button
          id="lottie-play-button"
          @click=${()=>{this.togglePlay()}}
          class=${t||e?"active "+(this._hasMultipleAnimations?"btn-spacing-center":"btn-spacing-right"):""+(this._hasMultipleAnimations?"btn-spacing-center":"btn-spacing-right")}
          aria-label="play / pause animation"
        >
          ${t?F`
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M3.99996 2C3.26358 2 2.66663 2.59695 2.66663 3.33333V12.6667C2.66663 13.403 3.26358 14 3.99996 14H5.33329C6.06967 14 6.66663 13.403 6.66663 12.6667V3.33333C6.66663 2.59695 6.06967 2 5.33329 2H3.99996Z"
                    fill="#20272C"
                  />
                  <path
                    d="M10.6666 2C9.93025 2 9.33329 2.59695 9.33329 3.33333V12.6667C9.33329 13.403 9.93025 14 10.6666 14H12C12.7363 14 13.3333 13.403 13.3333 12.6667V3.33333C13.3333 2.59695 12.7363 2 12 2H10.6666Z"
                    fill="#20272C"
                  />
                </svg>
              `:F`
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M3.33337 3.46787C3.33337 2.52312 4.35948 1.93558 5.17426 2.41379L12.8961 6.94592C13.7009 7.41824 13.7009 8.58176 12.8961 9.05408L5.17426 13.5862C4.35948 14.0644 3.33337 13.4769 3.33337 12.5321V3.46787Z"
                    fill="#20272C"
                  />
                </svg>
              `}
        </button>
        ${this._hasMultipleAnimations?F`
              <button @click=${()=>this.next()} aria-label="Next animation" class="btn-spacing-right">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    fill-rule="evenodd"
                    clip-rule="evenodd"
                    d="M14.3336 2.5C14.3336 2.22386 14.1097 2 13.8336 2C13.5574 2 13.3336 2.22386 13.3336 2.5V13.5C13.3336 13.7761 13.5574 14 13.8336 14C14.1097 14 14.3336 13.7761 14.3336 13.5V2.5ZM3.50618 2.21722C2.83954 1.82595 2 2.30667 2 3.07965V12.9201C2 13.6931 2.83954 14.1738 3.50618 13.7825L11.8893 8.86231C12.5477 8.47586 12.5477 7.52389 11.8893 7.13745L3.50618 2.21722Z"
                    fill="#20272C"
                  />
                </svg>
              </button>
            `:F``}
        <input
          id="lottie-seeker-input"
          class="seeker ${-1===this._dotLottieCommonPlayer?.direction?"to-left":""}"
          type="range"
          min="0"
          step="1"
          max="100"
          .value=${this._seeker}
          @input=${t=>this._handleSeekChange(t)}
          @mousedown=${()=>{this._freeze()}}
          @mouseup=${()=>{this._dotLottieCommonPlayer?.unfreeze()}}
          aria-valuemin="1"
          aria-valuemax="100"
          role="slider"
          aria-valuenow=${this._seeker}
          aria-label="lottie-seek-input"
          style=${`--seeker: ${this._seeker}`}
        />
        <button
          id="lottie-loop-toggle"
          @click=${()=>this.toggleLooping()}
          class=${this._dotLottieCommonPlayer?.loop?"active btn-spacing-left":"btn-spacing-left"}
          aria-label="loop-toggle"
        >
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M10.8654 2.31319C11.0607 2.11793 11.3772 2.11793 11.5725 2.31319L13.4581 4.19881C13.6534 4.39407 13.6534 4.71066 13.4581 4.90592L11.5725 6.79154C11.3772 6.9868 11.0607 6.9868 10.8654 6.79154C10.6701 6.59628 10.6701 6.27969 10.8654 6.08443L11.6162 5.33362H4V6.66695C4 7.03514 3.70152 7.33362 3.33333 7.33362C2.96514 7.33362 2.66666 7.03514 2.66666 6.66695L2.66666 4.66695C2.66666 4.29876 2.96514 4.00028 3.33333 4.00028H11.8454L10.8654 3.0203C10.6701 2.82504 10.6701 2.50846 10.8654 2.31319Z"
              fill="currentColor"
            />
            <path
              d="M12.4375 11.9999C12.8057 11.9999 13.1042 11.7014 13.1042 11.3332V9.33321C13.1042 8.96502 12.8057 8.66655 12.4375 8.66655C12.0693 8.66655 11.7708 8.96502 11.7708 9.33321V10.6665H4.15462L4.90543 9.91573C5.10069 9.72047 5.10069 9.40389 4.90543 9.20862C4.71017 9.01336 4.39359 9.01336 4.19832 9.20862L2.31271 11.0942C2.11744 11.2895 2.11744 11.6061 2.31271 11.8013L4.19832 13.687C4.39359 13.8822 4.71017 13.8822 4.90543 13.687C5.10069 13.4917 5.10069 13.1751 4.90543 12.9799L3.92545 11.9999H12.4375Z"
              fill="currentColor"
            />
          </svg>
        </button>
        ${this._hasMultipleAnimations||this._hasMultipleThemes||this._hasMultipleStates?F`
              <button
                id="lottie-animation-options"
                @click=${()=>{this._popoverIsOpen=!this._popoverIsOpen,this.requestUpdate()}}
                aria-label="options"
                class="btn-spacing-right"
                style=${"background-color: "+(this._popoverIsOpen?"var(--lottie-player-toolbar-icon-hover-color)":"")}
              >
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M8.33337 11.6666C7.78109 11.6666 7.33337 12.1143 7.33337 12.6666C7.33337 13.2189 7.78109 13.6666 8.33337 13.6666C8.88566 13.6666 9.33337 13.2189 9.33337 12.6666C9.33337 12.1143 8.88566 11.6666 8.33337 11.6666Z"
                    fill="#20272C"
                  />
                  <path
                    d="M7.33337 7.99992C7.33337 7.44763 7.78109 6.99992 8.33337 6.99992C8.88566 6.99992 9.33338 7.44763 9.33338 7.99992C9.33338 8.5522 8.88566 8.99992 8.33337 8.99992C7.78109 8.99992 7.33337 8.5522 7.33337 7.99992Z"
                    fill="#20272C"
                  />
                  <path
                    d="M7.33337 3.33325C7.33337 2.78097 7.78109 2.33325 8.33337 2.33325C8.88566 2.33325 9.33338 2.78097 9.33338 3.33325C9.33338 3.88554 8.88566 4.33325 8.33337 4.33325C7.78109 4.33325 7.33337 3.88554 7.33337 3.33325Z"
                    fill="#20272C"
                  />
                </svg>
              </button>
            `:F``}
      </div>
      ${this._popoverIsOpen?F`
            <div
              id="popover"
              class="popover"
              tabindex="0"
              aria-label="lottie animations themes popover"
              style="min-height: ${this.themes().length>0?"84px":"auto"}"
            >
              ${this._animationsTabIsOpen||this._styleTabIsOpen||this._statesTabIsOpen?F``:F`
                    <button
                      class="popover-button"
                      tabindex="0"
                      aria-label="animations"
                      @click=${()=>{this._animationsTabIsOpen=!this._animationsTabIsOpen,this.requestUpdate()}}
                      @keydown=${t=>{("Space"===t.code||"Enter"===t.code)&&(this._animationsTabIsOpen=!this._animationsTabIsOpen,this.requestUpdate())}}
                    >
                      <div class="popover-button-text">Animations</div>
                      <div>
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M10.4697 17.5303C10.1768 17.2374 10.1768 16.7626 10.4697 16.4697L14.9393 12L10.4697 7.53033C10.1768 7.23744 10.1768 6.76256 10.4697 6.46967C10.7626 6.17678 11.2374 6.17678 11.5303 6.46967L16.5303 11.4697C16.8232 11.7626 16.8232 12.2374 16.5303 12.5303L11.5303 17.5303C11.2374 17.8232 10.7626 17.8232 10.4697 17.5303Z"
                            fill="#4C5863"
                          />
                        </svg>
                      </div>
                    </button>
                  `}
              ${!this._hasMultipleThemes||this._styleTabIsOpen||this._animationsTabIsOpen||this._statesTabIsOpen?"":F` <button
                    class="popover-button"
                    aria-label="Themes"
                    @click=${()=>{this._styleTabIsOpen=!this._styleTabIsOpen,this.requestUpdate()}}
                    @keydown=${t=>{("Space"===t.code||"Enter"===t.code)&&(this._styleTabIsOpen=!this._styleTabIsOpen,this.requestUpdate())}}
                  >
                    <div class="popover-button-text">Themes</div>
                    <div>
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          fill-rule="evenodd"
                          clip-rule="evenodd"
                          d="M10.4697 17.5303C10.1768 17.2374 10.1768 16.7626 10.4697 16.4697L14.9393 12L10.4697 7.53033C10.1768 7.23744 10.1768 6.76256 10.4697 6.46967C10.7626 6.17678 11.2374 6.17678 11.5303 6.46967L16.5303 11.4697C16.8232 11.7626 16.8232 12.2374 16.5303 12.5303L11.5303 17.5303C11.2374 17.8232 10.7626 17.8232 10.4697 17.5303Z"
                          fill="#4C5863"
                        />
                      </svg>
                    </div>
                  </button>`}
              ${!this._hasMultipleStates||this._styleTabIsOpen||this._animationsTabIsOpen||this._statesTabIsOpen?"":F` <button
                    class="popover-button"
                    aria-label="States"
                    @click=${()=>{this._statesTabIsOpen=!this._statesTabIsOpen,this.requestUpdate()}}
                    @keydown=${t=>{("Space"===t.code||"Enter"===t.code)&&(this._statesTabIsOpen=!this._statesTabIsOpen,this.requestUpdate())}}
                  >
                    <div class="popover-button-text">States</div>
                    <div>
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          fill-rule="evenodd"
                          clip-rule="evenodd"
                          d="M10.4697 17.5303C10.1768 17.2374 10.1768 16.7626 10.4697 16.4697L14.9393 12L10.4697 7.53033C10.1768 7.23744 10.1768 6.76256 10.4697 6.46967C10.7626 6.17678 11.2374 6.17678 11.5303 6.46967L16.5303 11.4697C16.8232 11.7626 16.8232 12.2374 16.5303 12.5303L11.5303 17.5303C11.2374 17.8232 10.7626 17.8232 10.4697 17.5303Z"
                          fill="#4C5863"
                        />
                      </svg>
                    </div>
                  </button>`}
              ${this._animationsTabIsOpen?F`<button
                      class="option-title-button"
                      aria-label="Back to main popover menu"
                      @click=${()=>{this._animationsTabIsOpen=!this._animationsTabIsOpen,this.requestUpdate()}}
                    >
                      <div class="option-title-chevron">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M13.5303 6.46967C13.8232 6.76256 13.8232 7.23744 13.5303 7.53033L9.06066 12L13.5303 16.4697C13.8232 16.7626 13.8232 17.2374 13.5303 17.5303C13.2374 17.8232 12.7626 17.8232 12.4697 17.5303L7.46967 12.5303C7.17678 12.2374 7.17678 11.7626 7.46967 11.4697L12.4697 6.46967C12.7626 6.17678 13.2374 6.17678 13.5303 6.46967Z"
                            fill="#20272C"
                          />
                        </svg>
                      </div>
                      <div>Animations</div>
                    </button>
                    <div class="option-title-separator"></div>
                    <div class="option-row">
                      <ul>
                        ${this.animations().map((t=>F`
                            <li>
                              <button
                                class="option-button"
                                aria-label=${`${t}`}
                                @click=${()=>{this._animationsTabIsOpen=!this._animationsTabIsOpen,this._popoverIsOpen=!this._popoverIsOpen,this.play(t),this.requestUpdate()}}
                                @keydown=${e=>{("Space"===e.code||"Enter"===e.code)&&(this._animationsTabIsOpen=!this._animationsTabIsOpen,this._popoverIsOpen=!this._popoverIsOpen,this.play(t),this.requestUpdate())}}
                              >
                                <div class="option-tick">
                                  ${this.currentAnimation()===t?F`
                                        <svg
                                          width="24"
                                          height="24"
                                          viewBox="0 0 24 24"
                                          fill="none"
                                          xmlns="http://www.w3.org/2000/svg"
                                        >
                                          <path
                                            fill-rule="evenodd"
                                            clip-rule="evenodd"
                                            d="M20.5281 5.9372C20.821 6.23009 20.821 6.70497 20.5281 6.99786L9.46297 18.063C9.32168 18.2043 9.12985 18.2833 8.93004 18.2826C8.73023 18.2819 8.53895 18.2015 8.39864 18.0593L3.46795 13.0596C3.1771 12.7647 3.1804 12.2898 3.47532 11.999C3.77024 11.7081 4.2451 11.7114 4.53595 12.0063L8.93634 16.4683L19.4675 5.9372C19.7604 5.64431 20.2352 5.64431 20.5281 5.9372Z"
                                            fill="#20272C"
                                          />
                                        </svg>
                                      `:F`<div style="width: 24px; height: 24px"></div>`}
                                </div>
                                <div>${t}</div>
                              </button>
                            </li>
                          `))}
                      </ul>
                    </div> `:F``}
              ${this._styleTabIsOpen?F`<div class="option-title-themes-row">
                      <button
                        class="option-title-button themes"
                        aria-label="Back to main popover menu"
                        @click=${()=>{this._styleTabIsOpen=!this._styleTabIsOpen,this.requestUpdate()}}
                      >
                        <div class="option-title-chevron">
                          <svg
                            width="24"
                            height="24"
                            viewBox="0 0 24 24"
                            fill="none"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M13.5303 6.46967C13.8232 6.76256 13.8232 7.23744 13.5303 7.53033L9.06066 12L13.5303 16.4697C13.8232 16.7626 13.8232 17.2374 13.5303 17.5303C13.2374 17.8232 12.7626 17.8232 12.4697 17.5303L7.46967 12.5303C7.17678 12.2374 7.17678 11.7626 7.46967 11.4697L12.4697 6.46967C12.7626 6.17678 13.2374 6.17678 13.5303 6.46967Z"
                              fill="#20272C"
                            />
                          </svg>
                        </div>
                        <div class="option-title-text">Themes</div>
                        ${""===this._dotLottieCommonPlayer?.defaultTheme?F``:F`
                              <button
                                class="reset-btn"
                                @click=${()=>{this.setTheme(""),this.requestUpdate()}}
                              >
                                Reset
                              </button>
                            `}
                      </button>
                    </div>
                    <div class="option-title-separator"></div>
                    <div class="option-row">
                      <ul>
                        ${this._themesForCurrentAnimation.map((t=>F`
                            <li>
                              <button
                                class="option-button"
                                aria-label="${t.id}"
                                @click=${()=>{this.setTheme(t.id)}}
                                @keydown=${e=>{("Space"===e.code||"Enter"===e.code)&&this.setTheme(t.id)}}
                              >
                                <div class="option-tick">
                                  ${this.getDefaultTheme()===t.id?F`
                                        <svg
                                          width="24"
                                          height="24"
                                          viewBox="0 0 24 24"
                                          fill="none"
                                          xmlns="http://www.w3.org/2000/svg"
                                        >
                                          <path
                                            fill-rule="evenodd"
                                            clip-rule="evenodd"
                                            d="M20.5281 5.9372C20.821 6.23009 20.821 6.70497 20.5281 6.99786L9.46297 18.063C9.32168 18.2043 9.12985 18.2833 8.93004 18.2826C8.73023 18.2819 8.53895 18.2015 8.39864 18.0593L3.46795 13.0596C3.1771 12.7647 3.1804 12.2898 3.47532 11.999C3.77024 11.7081 4.2451 11.7114 4.53595 12.0063L8.93634 16.4683L19.4675 5.9372C19.7604 5.64431 20.2352 5.64431 20.5281 5.9372Z"
                                            fill="#20272C"
                                          />
                                        </svg>
                                      `:F`<div style="width: 24px; height: 24px"></div>`}
                                </div>
                                <div>${t.id}</div>
                              </button>
                            </li>
                          `))}
                      </ul>
                    </div>`:F``}
              ${this._statesTabIsOpen?F`<div class="option-title-themes-row">
                      <button
                        class="option-title-button themes"
                        aria-label="Back to main popover menu"
                        @click=${()=>{this._statesTabIsOpen=!this._statesTabIsOpen,this.requestUpdate()}}
                      >
                        <div class="option-title-chevron">
                          <svg
                            width="24"
                            height="24"
                            viewBox="0 0 24 24"
                            fill="none"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M13.5303 6.46967C13.8232 6.76256 13.8232 7.23744 13.5303 7.53033L9.06066 12L13.5303 16.4697C13.8232 16.7626 13.8232 17.2374 13.5303 17.5303C13.2374 17.8232 12.7626 17.8232 12.4697 17.5303L7.46967 12.5303C7.17678 12.2374 7.17678 11.7626 7.46967 11.4697L12.4697 6.46967C12.7626 6.17678 13.2374 6.17678 13.5303 6.46967Z"
                              fill="#20272C"
                            />
                          </svg>
                        </div>
                        <div class="option-title-text">States</div>
                        <button
                          class="reset-btn"
                          @click=${()=>{this.exitInteractiveMode(),this.requestUpdate()}}
                        >
                          Reset
                        </button>
                      </button>
                    </div>
                    <div class="option-title-separator"></div>
                    <div class="option-row">
                      <ul>
                        ${this._statesForCurrentAnimation.map((t=>F`
                            <li>
                              <button
                                class="option-button"
                                aria-label="${t}"
                                @click=${()=>{this.enterInteractiveMode(t)}}
                                @keydown=${e=>{("Space"===e.code||"Enter"===e.code)&&this.enterInteractiveMode(t)}}
                              >
                                <div class="option-tick">
                                  ${this.getActiveStateMachine()===t?F`
                                        <svg
                                          width="24"
                                          height="24"
                                          viewBox="0 0 24 24"
                                          fill="none"
                                          xmlns="http://www.w3.org/2000/svg"
                                        >
                                          <path
                                            fill-rule="evenodd"
                                            clip-rule="evenodd"
                                            d="M20.5281 5.9372C20.821 6.23009 20.821 6.70497 20.5281 6.99786L9.46297 18.063C9.32168 18.2043 9.12985 18.2833 8.93004 18.2826C8.73023 18.2819 8.53895 18.2015 8.39864 18.0593L3.46795 13.0596C3.1771 12.7647 3.1804 12.2898 3.47532 11.999C3.77024 11.7081 4.2451 11.7114 4.53595 12.0063L8.93634 16.4683L19.4675 5.9372C19.7604 5.64431 20.2352 5.64431 20.5281 5.9372Z"
                                            fill="#20272C"
                                          />
                                        </svg>
                                      `:F`<div style="width: 24px; height: 24px"></div>`}
                                </div>
                                <div>${t}</div>
                              </button>
                            </li>
                          `))}
                      </ul>
                    </div>`:F``}
            </div>
          `:F``}
    `}render(){let t=this.controls?"main controls":"main",e=this.controls?"animation controls":"animation";return F`
      <div
        id="animation-container"
        class=${t}
        lang="en"
        role="img"
        aria-label="lottie-animation-container"
        lottietype=${this.lottietype}
      >
        <div id="animation" class=${e} style="background:${this.background};">
          ${this._dotLottieCommonPlayer?.currentState===lt.f.Error?F` <div class="error">⚠️</div> `:void 0}
        </div>
        ${this.controls?this.renderControls():void 0}
      </div>
    `}};(0,ht.a)([ut({type:String})],_t.prototype,"defaultTheme",2),(0,ht.a)([(ct="#animation",(({finisher:t,descriptor:e})=>(i,o)=>{var n;if(void 0===o){let o=null!==(n=i.originalKey)&&void 0!==n?n:i.key,r=null!=e?{kind:"method",placement:"prototype",key:o,descriptor:e(i.key)}:{...i,key:o};return null!=t&&(r.finisher=function(e){t(e,o)}),r}{let n=i.constructor;void 0!==e&&Object.defineProperty(i,o,e(o)),t?.(n,o)}})({descriptor:t=>{let e={get(){var t,e;return null!==(e=null===(t=this.renderRoot)||void 0===t?void 0:t.querySelector(ct))&&void 0!==e?e:null},enumerable:!0,configurable:!0};if(ft){let i="symbol"==typeof t?Symbol():"__"+t;e.get=function(){var t,e;return void 0===this[i]&&(this[i]=null!==(e=null===(t=this.renderRoot)||void 0===t?void 0:t.querySelector(ct))&&void 0!==e?e:null),this[i]}}return e}}))],_t.prototype,"container",2),(0,ht.a)([ut()],_t.prototype,"playMode",2),(0,ht.a)([ut({type:Boolean})],_t.prototype,"autoplay",2),(0,ht.a)([ut({type:String})],_t.prototype,"background",2),(0,ht.a)([ut({type:String})],_t.prototype,"lottietype",2),(0,ht.a)([ut({type:Boolean})],_t.prototype,"controls",2),(0,ht.a)([ut({type:Number})],_t.prototype,"direction",2),(0,ht.a)([ut({type:Boolean})],_t.prototype,"hover",2),(0,ht.a)([ut({type:String})],_t.prototype,"loop",2),(0,ht.a)([ut({type:String})],_t.prototype,"renderer",2),(0,ht.a)([ut({type:Number})],_t.prototype,"speed",2),(0,ht.a)([ut({type:String})],_t.prototype,"src",2),(0,ht.a)([ut()],_t.prototype,"intermission",2),(0,ht.a)([ut({type:String})],_t.prototype,"activeAnimationId",2),(0,ht.a)([ut({type:Boolean})],_t.prototype,"light",2),(0,ht.a)([ut({type:Boolean})],_t.prototype,"worker",2),(0,ht.a)([ut({type:String})],_t.prototype,"activeStateId",2),(0,ht.a)([function(t){return ut({...t,state:!0})}()],_t.prototype,"_seeker",2),customElements.get(vt)||customElements.define(vt,_t)}}]);