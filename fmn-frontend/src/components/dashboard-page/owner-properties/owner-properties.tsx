import { Component, h, Prop, State, Event, EventEmitter } from '@stencil/core';
import { Property } from '../../../models/interfaces';

@Component({
  tag: 'owner-properties',
  styleUrl: 'owner-properties.css',
  shadow: true,
})
export class OwnerProperties {
  @State() properties: Property[] = [];
  @State() showAddModal = false;
  @Prop() user: number;

  @Event() changeNav: EventEmitter<string>;

  handleChangeNav(nav: string) {
    this.changeNav.emit(nav);
  }

  async componentWillLoad() {
    if (!this.user) {
      console.warn('No userId provided');
      return;
    }

    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error('No JWT token found');
        return;
      }
      const response = await fetch(`http://localhost:8080/owners/getProperties/${this.user}`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      this.properties = Array.isArray(data) ? data : [];
      console.log('Owner properties after fetch:', this.properties);
    } catch (err) {
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

            <stencil-route-link url="/dashboard/add-property">
              <button class="add-btn" onClick={() => this.handleChangeNav('add-property')}>
                Add New Property{' '}
              </button>
            </stencil-route-link>
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
