angular.module('radarApp')
    .controller('RadarCoverageController', ['$scope', 'radarService', 'webSocketService', function($scope, radarService, webSocketService) {
        // Layer for managing multiple radar coverages
        const coverageLayer = new ol.layer.Vector({
            source: new ol.source.Vector(),
            style: function(feature) {
                return new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: feature.get('selected') ? 'rgba(255, 0, 0, 0.4)' : 'rgba(255, 0, 0, 0.2)'
                    }),
                    stroke: new ol.style.Stroke({
                        color: feature.get('selected') ? '#ff0000' : '#ff6666',
                        width: feature.get('selected') ? 3 : 2
                    })
                });
            }
        });

        // Initialize coverage features map
        const coverageFeatures = new Map();

        // Function to update single radar coverage
        function updateRadarCoverage(radar) {
            const format = new ol.format.WKT();
            const feature = format.readFeature(radar.coverage, {
                dataProjection: 'EPSG:4326',
                featureProjection: 'EPSG:3857'
            });

            // Set radar properties on feature
            feature.setProperties({
                radarId: radar.id,
                name: radar.name,
                selected: false
            });

            // Store or update feature
            if (coverageFeatures.has(radar.id)) {
                coverageLayer.getSource().removeFeature(coverageFeatures.get(radar.id));
            }
            coverageFeatures.set(radar.id, feature);
            coverageLayer.getSource().addFeature(feature);
        }

        // Function to update all radar coverages
        function updateAllRadarCoverages() {
            radarService.getRadarData().then(function(radars) {
                coverageLayer.getSource().clear();
                coverageFeatures.clear();
                radars.forEach(updateRadarCoverage);
            });
        }

        // Function to highlight selected radar coverage
        function selectRadarCoverage(radarId) {
            coverageFeatures.forEach(function(feature) {
                feature.set('selected', feature.get('radarId') === radarId);
            });
        }

        // Initialize coverage layer
        $scope.initializeCoverageLayer = function(map) {
            map.addLayer(coverageLayer);
            updateAllRadarCoverages();

            // Add click interaction
            map.on('click', function(event) {
                const feature = map.forEachFeatureAtPixel(event.pixel, function(feature) {
                    return feature;
                });

                if (feature) {
                    const radarId = feature.get('radarId');
                    selectRadarCoverage(radarId);
                    $scope.$emit('radarSelected', radarId);
                }
            });
        };

        // Initialize WebSocket connection
        webSocketService.connect();

        // Handle WebSocket events
        $scope.$on('radarUpdate', function(event, radar) {
            updateRadarCoverage(radar);
        });

        $scope.$on('radarDelete', function(event, radarId) {
            if (coverageFeatures.has(radarId)) {
                coverageLayer.getSource().removeFeature(coverageFeatures.get(radarId));
                coverageFeatures.delete(radarId);
            }
        });

        // Cleanup WebSocket connection when controller is destroyed
        $scope.$on('$destroy', function() {
            webSocketService.disconnect();
        });
    }]);