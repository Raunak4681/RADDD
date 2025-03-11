angular.module('radarApp')
    .service('radarService', ['$http', 'API_URL', function($http, API_URL) {
        this.getRadarData = function() {
            return $http.get(API_URL + '/radar')
                .then(function(response) {
                    return response.data;
                });
        };

        this.updateRadarData = function(radarData) {
            return $http.post(API_URL + '/radar', radarData)
                .then(function(response) {
                    return response.data;
                });
        };
    }]);