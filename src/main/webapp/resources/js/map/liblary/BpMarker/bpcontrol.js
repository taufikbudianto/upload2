// BpControl 0.13 Copyright 2006 BitPerfect http://www.gmaptools.com - All rights reserved.
function BpControl(){var _a=0;function l(_b,_c,_d,_f){if(!_b)_b='Loading...';if(!_c)_c=new GSize(100,100);if(!_d)_d=G_ANCHOR_TOP_LEFT;this._b=_b;this._c=_c;this._d=_d;this._f=_f;this._a=++_a;this._g='bpcontrol'+this._a;}l.prototype=new GControl();var _h=l.prototype;_h.getDivId=function(){return this._g;};_h.getDiv=function(){return this._i;};_h.getContent=function(){return this._i.innerHTML;};_h.setContent=function(_b){this._i.innerHTML=_b;};_h.show=function(){this._i.style.display='';};_h.hide=function(){this._i.style.display='none';};_h.getClassName=function(){return this._f;};_h.setClassName=function(_f){if(!this._f)return;this._f=_f;this._i.className=_f;};_h.initialize=function(_j){var _i=document.createElement('div');_i.setAttribute('id',this._a);var _k=_i.style;_k.display='none';if(this._f){_i.className=this._f;}else{_k.border='1px solid black';_k.backgroundColor='white';_k.fontWeight='bold';_k.paddingLeft='3px';_k.paddingRight='3px';}_i.innerHTML=this._b;_j.getContainer().appendChild(_i);this._i=_i;return _i;};_h.getDefaultPosition=function(){return new GControlPosition(this._d,this._c);};_h.printable=function(){return false;};_h.selectable=function(){return false;};window.BpControl=l;}BpControl();