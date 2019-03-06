import React, { Component } from 'react';
import { BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import {Dropdown, DropdownButton, MenuItem} from 'react-bootstrap';
import generateData from './../Tables/generateData';
import Rodal from 'rodal';
import 'rodal/lib/rodal.css';
import Map from '../MapsPage/Map';


import db from '../../components/firebase/firebase';
import rodal from 'rodal';
import { GoogleMap } from 'react-google-maps';

const data = generateData(1000);

const level = {
  'head': 0,
  'co head': 1,
  'worker': 2
}

function rowStyleFormat(row, rowIdx) {
  // console.log(row);
  
  if(row == undefined) return { backgroundColor: 'white' };
  // if(typeof row == 'undefined') return;
  // console.log(row.available);
  return { backgroundColor: row.available == true ? 'green' : 'red' };
}

let locRef;

class ReactBootstrapTable extends Component {

  state = {
    data: [],
    visible: false,
    selectedOfficerId: null,
    location: {}
  };


  handleDeptSelect = (eventKey) => {
    db.ref('officer').on('value',snap => {
      let data = [];
      let i = 0;
      Object.entries(snap.val()).forEach(([key, val]) => {
        if(val.name){
          data.push({
            key: key,
            id: i++,
            name: val.name,
            dept: val.department,
            title: val.title,
            available: val.available
          })
        }
      })
      if(eventKey != 'all')
        data = data.filter(x => x.dept == eventKey);
      this.setState({data});
    })
  }
  componentDidMount() {
    db.ref('officer').on('value',snap => {
      let data = [];
      Object.entries(snap.val()).forEach(([key, val]) => {
        if(val.name){
          data.push({
            key: key,
            id: key.substring(key.length - 4),
            name: val.name,
            dept: val.department,
            title: val.title,
            available: val.available
          })
        }
      })
      this.setState({data});
    })
  }

  show() {
    this.setState({visible: true});
  }

  hide() {
      console.log('hide');
      this.setState({ visible: false });
      db.ref(`officer/${this.state.selectedOfficerId}/loc`).off('value');
  }

  handleRowClick = (row) => {
    console.log('row clicked');
    console.log(row.key, row.name);
    this.setState({
      selectedOfficerId: row.key
    },() => {
      console.log(`officer/${this.state.selectedOfficerId}/loc`);
      locRef = db.ref(`officer/${this.state.selectedOfficerId}/loc`)
      .on('value',snap => {
        window.alert(JSON.stringify(snap.val()))
      })
    })
    
  }

  render() {

    const { data } = this.state;
    const options = {
      sizePerPage: 20,
      prePage: 'Previous',
      nextPage: 'Next',
      firstPage: 'First',
      lastPage: 'Last',
      hideSizePerPage: true,
    };
    
    return (
      <div>
        <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <div className="card">
              <div className="header">
                <h4>Check avalaibility</h4>
                <p>Check the avalaibility of a single user as well as an entire department</p>
              </div>
              <div className="content">
                <DropdownButton title="Department" onSelect={this.handleDeptSelect}>
                  <MenuItem eventKey='all'>All</MenuItem>
                  <MenuItem eventKey='approval'>Approval</MenuItem>
                  <MenuItem eventKey='academic'>Academic</MenuItem>
                  <MenuItem eventKey='finance'>Finance</MenuItem>
                  <MenuItem eventKey='administrative'>Administrative</MenuItem>
                </DropdownButton>
                <BootstrapTable
                  data={data}
                  bordered={false}
                  striped
                  pagination={true}
                  options={options}
                  trStyle={rowStyleFormat}
                  options = {{onRowClick: this.handleRowClick}}
                  >
                  <TableHeaderColumn
                    dataField='id'
                    isKey
                    width="50px"
                    dataSort>
                    ID
                  </TableHeaderColumn>
                  <TableHeaderColumn
                    dataField='name'
                    width="10%"
                    filter={ { type: 'TextFilter'} }
                    dataSort>
                    Name
                  </TableHeaderColumn>
                  <TableHeaderColumn
                    dataField='title'
                    width="10%"
                    dataSort>
                    Title
                  </TableHeaderColumn>
                  <TableHeaderColumn
                    dataField='dept'
                    width="10%"
                    dataSort>
                    Department
                  </TableHeaderColumn>
                  <TableHeaderColumn
                    dataField='available'
                    width="10%"
                    dataSort>
                    Availaibility
                  </TableHeaderColumn>
                </BootstrapTable>
              </div>
            </div>
          </div>
        </div>
      </div>
      </div>
    );
  }
}
export default ReactBootstrapTable;
