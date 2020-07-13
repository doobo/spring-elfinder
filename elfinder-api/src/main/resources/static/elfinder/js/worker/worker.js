self.onmessage = function(e) {
	var d = e.data;
	try {
		self.data = d.data;
		if (d.scripts) {
			for(var i = 0; i < d.scripts.length; i++) {
				importScripts(d.scripts[i]);
			}
		}
		self.postMessage(self.res);
	} catch (e) {
		self.postMessage({error: e.toString()});
	}
};