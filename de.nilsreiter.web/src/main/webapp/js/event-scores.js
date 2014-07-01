var graph = new Rickshaw.Graph({
	element: document.querySelector("#chart"),
	renderer: 'line',
	width:500,
	series: [{
		data: [ { x: 0, y: 40 }, { x: 1, y: 49 } ],
		color: 'steelblue'
	}, {
		data: [ { x: 0, y: 20 }, { x: 1, y: 24 } ],
		color: 'lightblue'
	}]
});
 
graph.render();
