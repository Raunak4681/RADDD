angular.module('radarApp')
    .controller('RadarFormController', ['$scope', '$rootScope', 'radarService', function($scope, $rootScope, radarService) {
        $scope.radarParams = {
            longitude: 0,
            latitude: 0,
            range: 100,
            angle: 360
        };

        // Initialize map reference when the map is ready
        $scope.$on('initializeMap', function(event, map) {
            $scope.map = map;
        });

        $scope.goToLocation = function() {
            var map = $scope.map || $rootScope.map;
            if (!map) {
                console.error('Map not initialized');
                return;
            }

            // Validate coordinates
            if (!isValidCoordinates($scope.radarParams.longitude, $scope.radarParams.latitude)) {
                console.error('Invalid coordinates');
                return;
            }

            const coordinates = [$scope.radarParams.longitude, $scope.radarParams.latitude];
            map.getView().animate({
                center: ol.proj.fromLonLat(coordinates),
                zoom: 12,
                duration: 1000
            });

            // Submit radar data to backend
            radarService.updateRadarData($scope.radarParams).then(function(response) {
                $rootScope.$broadcast('radarUpdated', response);
            }).catch(function(error) {
                console.error('Error updating radar data:', error);
            });
        };

        function isValidCoordinates(longitude, latitude) {
            return longitude >= -180 && longitude <= 180 && 
                   latitude >= -90 && latitude <= 90;
        }
    }]);