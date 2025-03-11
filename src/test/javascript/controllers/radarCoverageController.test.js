describe('RadarCoverageController', function() {
    let $scope, $rootScope, radarService, webSocketService, controller;
    let map, coverageLayer;

    beforeEach(module('radarApp'));

    beforeEach(inject(function(_$rootScope_, _$controller_, _radarService_, _webSocketService_) {
        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        radarService = _radarService_;
        webSocketService = _webSocketService_;

        // Mock OpenLayers map and layer
        map = {
            addLayer: jasmine.createSpy('addLayer'),
            on: jasmine.createSpy('on'),
            forEachFeatureAtPixel: jasmine.createSpy('forEachFeatureAtPixel')
        };

        controller = _$controller_('RadarCoverageController', {
            $scope: $scope,
            radarService: radarService,
            webSocketService: webSocketService
        });
    }));

    describe('initialization', function() {
        it('should initialize coverage layer when map is provided', function() {
            spyOn(radarService, 'getRadarData').and.returnValue(Promise.resolve([]));
            
            $scope.initializeCoverageLayer(map);

            expect(map.addLayer).toHaveBeenCalled();
            expect(radarService.getRadarData).toHaveBeenCalled();
        });

        it('should connect to WebSocket on initialization', function() {
            spyOn(webSocketService, 'connect');
            
            expect(webSocketService.connect).toHaveBeenCalled();
        });
    });

    describe('radar coverage updates', function() {
        it('should update radar coverage when receiving WebSocket update', function() {
            const testRadar = {
                id: 1,
                coverage: 'POINT(-74 40)',
                name: 'Test Radar'
            };

            $scope.$emit('radarUpdate', testRadar);
            $scope.$digest();

            // Verify coverage is updated (implementation specific assertions)
        });

        it('should remove radar coverage when receiving delete event', function() {
            const radarId = 1;

            $scope.$emit('radarDelete', radarId);
            $scope.$digest();

            // Verify coverage is removed (implementation specific assertions)
        });
    });

    describe('cleanup', function() {
        it('should disconnect WebSocket when controller is destroyed', function() {
            spyOn(webSocketService, 'disconnect');

            $scope.$destroy();

            expect(webSocketService.disconnect).toHaveBeenCalled();
        });
    });
});