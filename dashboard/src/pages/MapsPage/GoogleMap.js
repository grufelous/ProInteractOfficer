import React from 'react';
import Map from './Map';

let MyMapComponent = (props) => (
    <div className="row">
      <div className="col-md-12">
        <div className="card">
          <div className="header">
            <h4>Live location</h4>
          </div>
          <div className="content">
            <div style={{ width: '400px', height: '400px', boxSizing: 'border-box' }}>
              <Map isMarkerShown/>
            </div>
          </div>
        </div>
      </div>
    </div>
);

export default MyMapComponent;