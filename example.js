function setup() {
    function foo(params, callback) {
        var a = params.a;
        callback();
    }

    foo({a: 12}, function () {
    });
}
function route() {
    drone.fly({speed: 15, time: 3}, function () {
        drone.fly({speed: 80, time: 6}, function () {
            drone.fly({speed: 20, time: 1}, function () {
            });
        });
    });
}