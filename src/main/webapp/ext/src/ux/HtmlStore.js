Ext.ns("Ext.data");
Ext.data.HtmlStore = Ext.extend(Ext.util.Observable, {
	panel : null,
	totalCount : 0,
	count : 0,
	proxy : {
		url: 'data.html',
		extraParams:{}
	},
	constructor : function(config) {
		Ext.apply(this, config);
		this.constructor.superclass.constructor.apply(this, arguments);
		this.addEvents(['beforeload', 'load', 'loadexception']);
	},
	getTotalCount : function(){
		return this.totalCount;
	},
	getCount : function() {
		return this.count;
	},
	load : function(options) {
		options = options || {params:{start:0,limit:20}};
		if(this.fireEvent("beforeload", this, options) !== false){
			this.storeOptions(options);
			var p = Ext.apply(options.params || {}, this.baseParams);
			p = Ext.applyIf(p, this.proxy.extraParams);
			Ext.Ajax.request({
				url: this.proxy.url,
				success: this.onSuccess,
				failure: this.onFailure,
				scope : this,
				params: p
			});
			
			return true;
		} else {
			return false;
		}
	},
	onFailure: function(response, options) {
		if (me.panel){
			var el = me.panel.el;
			el.mask();
		}
		this.fireEvent("loadexception", this, null, options);
	},
	onSuccess : function(response, options) {
		var me = this;
		if (me.panel){
			var el = me.panel.el;
			el.unmask();
			el.update(response.responseText);
			me.totalCount = parseInt(el.query("div[action=totalcount]")[0].innerHTML);
			me.count = parseInt(el.query("div[action=count]")[0].innerHTML);
		}
		me.fireEvent("load", me, null, options);
	},
	storeOptions : function(o){
		o = Ext.apply({}, o);
		delete o.callback;
		delete o.scope;
		this.lastOptions = o;
	}
});

Ext.reg('htmlstore', Ext.data.HtmlStore);
