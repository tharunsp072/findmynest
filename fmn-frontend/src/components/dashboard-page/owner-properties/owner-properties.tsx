import { Component, h, Prop, State, Event, EventEmitter, Method } from '@stencil/core';
import { Property } from '../property-listing/property';
import { propertyData } from '../property-listing/property-data';

@Component({
  tag: 'owner-properties',
  styleUrl: 'owner-properties.css',
  shadow: true,
})
export class OwnerProperties {
  @State() properties: Property[] = [];
  @State() showAddModal = false;
  @Prop() user :number;
  // decodeToken(token: string) {
  //   try {
  //     const payload = token.split('.')[1];
  //     const decoded = JSON.parse(atob(payload));
  //     return decoded;
  //   } catch (e) {
  //     console.error('Invalid token', e);
  //     return null;
  //   }
  // }
  async componentWillLoad() {
    if (!this.user) {
    console.warn('No userId provided');
    return;}

    try{
      const token = localStorage.getItem('token');
       if (!token) {
         console.error('No JWT token found');
         return;
       }
      const response = await fetch(`http://localhost:8080/owners/getProperties/${this.user}`,
       { method:'GET',
        headers : {
          'Authorization':`Bearer ${token}`
        }}
      );
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
      const data = await response.json();
      this.properties = Array.isArray(data) ? data : []; 
      console.log('Owner properties after fetch:', this.properties);
    }
    catch (err){
      console.error('Error fetching Properties');
    }
  }
  render() {
    return (
      <div class="layout">
        <property-listing source="owner" listingProperties={this.properties} user={this.user}></property-listing>
        {this.properties.length === 0 && (
          <div class="no-properties">
            <h1>No properties Listed</h1>
            <button onClick={() => (this.showAddModal = true)}>Add New Property</button>
          </div>
        )}

        {this.showAddModal && (
          <div class="modal-overlay">
            <div class="modal-content">
              <add-property-form source="owner"></add-property-form>
            </div>
            <button onClick={() => (this.showAddModal = false)}>Close</button>
          </div>
        )}
      </div>
    );
  }
}
