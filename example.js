function setup() {
}
function route() {
    fly({}, function () {
        fly({}, function () {
            fly({}, function () {
                route();
            })
        })
    })
}