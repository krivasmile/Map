window.onload = init;

function init() {
    let view = new ol.View({
        center: ol.proj.fromLonLat([32.5260800, 48.4538900]),
        zoom: 5.5,
    });

    let pointStyle = new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            src: 'data/image/marker.png',
            scale: 0.05,
        }),
    });

    let markerSource = new ol.source.Vector({});

    // parsing JSON
    let finish = JSON.parse(locat);

    // adding markers and click listener
    for (let i = 0; i < finish.length; i++){
        for(let y = 0; y < finish[i].coordinatesList.length; y++){
            let longitude = finish[i].coordinatesList[y].longitude;
            let latitude = finish[i].coordinatesList[y].latitude;
            let address = finish[i].coordinatesList[y].address;
            let addressDetails = finish[i].coordinatesList[y].addressDetails;
             let city = new ol.Feature({
                 geometry: new ol.geom.Point(ol.proj.fromLonLat([longitude, latitude])),
                 name: address + ', ' + addressDetails,
             });
             city.setStyle(pointStyle);
             markerSource.addFeature(city);

             let cityNameFromBD = finish[i].city;
             let cityButton = document.getElementById(cityNameFromBD);
             cityButton.addEventListener(
                'click',
                function () {
                    let focus = city.getGeometry();
                    view.fit(focus, {padding: [170, 50, 30, 150], minResolution: 150});
                },
                false
            );
        }
    }

    let markerLayer = new ol.layer.Vector({
        source: markerSource,
    });

    // main map
    let map = new ol.Map({
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM(),
            }),
            markerLayer],
        target: 'map',
        view: view,
    });

    // creating popup
    let element = document.getElementById('popup');

    let popup = new ol.Overlay({
        element: element,
        positioning: 'bottom-center',
        stopEvent: false,
        offset: [0, -10],
    });
    map.addOverlay(popup);

    // display popup on click
    map.on('click', function (evt) {
        let feature = map.forEachFeatureAtPixel(evt.pixel, function (feature) {
            return feature;
        });
        if (feature) {
            let coordinates = feature.getGeometry().getCoordinates();
            popup.setPosition(coordinates);
            $(element).popover({
                placement: 'top',
                html: true,
                content: feature.get('name'),
            });
            $(element).popover('show');
        } else {
            $(element).popover('dispose');
        }
    });

    // change mouse cursor when over marker
    map.on('pointermove', function (e) {
        if (e.dragging) {
            $(element).popover('dispose');
            return;
        }
        let pixel = map.getEventPixel(e.originalEvent);
        let hit = map.hasFeatureAtPixel(pixel);
        map.getTargetElement().style.cursor = hit ? 'pointer' : '';
    });

    // reset to Ukraine
    let resetToUkraine = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([32.5260800, 47.4538900])),
    });

    let zoomtoukraine = document.getElementById('zoomtoukraine');
    zoomtoukraine.addEventListener(
        'click',
        function () {
            let reset = resetToUkraine.getGeometry();
            view.fit(reset, {padding: [170, 50, 30, 150], minResolution: 3100});
        },
        false
    );
}