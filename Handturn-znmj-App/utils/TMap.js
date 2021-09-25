export function TMap(key) {
        return new Promise(function (resolve, reject) {
            window.init = function () {
            resolve(qq)//注意这里
        }
		console.log(2);
        var script = document.createElement("script");
		console.log(3);
        script.type = "text/javascript";
        script.src = "https://map.qq.com/api/js?v=2.exp&callback=init&key="+key;
        script.onerror = reject;
		console.log(4);
        document.head.appendChild(script);
      })
    }