Ext.onReady(function(){
	var task = {
			run : function() {
				Ext.get("backImg").dom.setAttribute("src","cn/wizool/bank/servlet/DocumentServlet?method=getCheckBack");
			},
			interval : 600000
		}
		var runner = new Ext.util.TaskRunner();
		runner.start(task);
})
