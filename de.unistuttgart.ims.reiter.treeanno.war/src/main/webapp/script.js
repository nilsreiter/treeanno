$.noty.defaults.layout = configuration["treeanno.ui.noty.layout"];
$.noty.defaults.theme = configuration["treeanno.ui.noty.theme"];
$.noty.defaults.timeout = configuration["treeanno.ui.noty.timeout"];

function init_all() {
	$("#topbar .right").buttonset();
	$(".nobutton").button({
		disabled:true
	});
}



function init_trans(fnc) {
	i18n.init({ 
		resGetPath:'locales/__ns__-__lng__.json',
		nsseparator:'::',
		lng: language.substring(0,2) }, function(t) {
			$("body").i18n();
			$(".trans").each(function(index, element) {
				var text = $(element).text().trim();
				$(element).empty();
				$(element).append(t(text));
			});
			if (fnc) fnc();
		});
}

// Source: http://stackoverflow.com/questions/280634/endswith-in-javascript
function ends_with(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}


