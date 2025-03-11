angular.module('radarApp', [])
    .constant('API_URL', 'http://localhost:8080/api')
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.defaults.headers.common['Content-Type'] = 'application/json';
    }])
    .run(['$rootScope', function($rootScope) {
        $rootScope.$on('initializeMap', function(event, map) {
            $rootScope.map = map;
        });
    }]);