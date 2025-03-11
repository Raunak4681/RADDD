angular.module('radarApp')
    .controller('MapController', ['$scope', '$rootScope', 'radarService', function($scope, $rootScope, radarService) {
        // Initialize OpenLayers map
        const map = new ol.Map({
            target: 'map',
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM()
                })
            ],
            view: new ol.View({
                center: ol.proj.fromLonLat([0, 0]),
                zoom: 2
            })
        });

        // Broadcast map instance to other controllers
        $scope.$broadcast('initializeMap', map);
        $rootScope.$broadcast('initializeMap', map);

        // Initialize radar coverage layer
        $scope.$broadcast('initializeCoverageLayer', map);

        // Watch for radar selection
        $scope.$on('radarSelected', function(event, radarId) {
            radarService.getRadarData(radarId).then(function(radar) {
                $scope.selectedRadar = radar;
                map.getView().animate({
                    center: ol.proj.fromLonLat([radar.longitude, radar.latitude]),
                    zoom: 12
                });
            });
        });
    }]);