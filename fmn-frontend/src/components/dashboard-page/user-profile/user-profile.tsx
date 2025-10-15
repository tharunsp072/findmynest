import { Component, h, Prop, State } from '@stencil/core';

@Component({
  tag: 'user-profile',
  styleUrl: 'user-profile.css',
  shadow: true,
})
export class UserProfile {
  @Prop() user: number;
  @Prop() role: string;

  @State() tenantData: any = null;
  @State() ownerData: any = null;
  @State() editMode: boolean = false;

  async componentWillLoad() {
    const token = localStorage.getItem('token');
    this.role = this.role.toLowerCase();
    try {
      if (this.role === 'tenant') {
        const response = await fetch(`http://localhost:8080/tenants/findTenant/${this.user}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error(`Failed to fetch tenant data: ${response.status}`);
     const text = await response.text();
     this.tenantData = text ? JSON.parse(text) : null;
      } else if (this.role === 'owner') {
        const response = await fetch(`http://localhost:8080/owners/findOwner/${this.user}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error(`Failed to fetch owner data: ${response.status}`);
        this.ownerData = await response.json();
      }
    } catch (err) {
      console.error('Error fetching profile:', err);
    }
  }

  handleInputChange(e: Event, field: string, type: 'tenant' | 'owner') {
    const target = e.target as HTMLInputElement;
    const value = target.value;

    if (type === 'tenant') {
      this.tenantData = { ...this.tenantData, [field]: value };
    } else {
      this.ownerData = { ...this.ownerData, [field]: value };
    }
  }

  toggleEdit() {
    this.editMode = !this.editMode;
  }

  async saveChanges() {
    const token = localStorage.getItem('token');
    const endpoint = this.role === 'tenant' ? `http://localhost:8080/tenants/updateTenant/${this.user}` : `http://localhost:8080/owners/updateOwner/${this.user}`;

    const bodyData = this.role === 'tenant' ? this.tenantData : this.ownerData;

    try {
      const response = await fetch(endpoint, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(bodyData),
      });

      if (!response.ok) throw new Error(`Failed to save: ${response.status}`);
      console.log('Profile updated successfully');
      this.editMode = false;
    } catch (err) {
      console.error('Error saving profile:', err);
    }
  }

  renderTenantProfile() {
    console.log(this.tenantData);
    const tenant = this.tenantData;
    if (!tenant) return <p>Loading tenant data...</p>;

    return (
      <div class="profile-container">
        <h2>Tenant Profile</h2>
        <label>
          Username:
          <input type="text" value={tenant.username} disabled={!this.editMode} onInput={e => this.handleInputChange(e, 'username', 'tenant')} />
        </label>

        <label>
          Contact Number:
          <input type="text" value={tenant.contact_number} disabled={!this.editMode} onInput={e => this.handleInputChange(e, 'contact_number', 'tenant')} />
        </label>

        <label>
          Preferences:
          <input type="text" value={tenant.preferences} disabled={!this.editMode} onInput={e => this.handleInputChange(e, 'preferences', 'tenant')} />
        </label>

        {tenant.owner && (
          <p>
            <b>Owner Name:</b> {tenant.owner.fullname}
          </p>
        )}

        {tenant.bookings && tenant.bookings.length > 0 ? (
          <div>
            <h3>Bookings:</h3>
            <ul>
              {tenant.bookings.map(b => (
                <li>
                  Booking ID: {b.bookingId} | Property: {b.property?.title || 'N/A'}
                </li>
              ))}
            </ul>
          </div>
        ) : (
          <p>No bookings found.</p>
        )}

        <div class="button-container">
          <button onClick={() => this.toggleEdit()}>{this.editMode ? 'Cancel' : 'Edit'}</button>
          {this.editMode && <button onClick={() => this.saveChanges()}>Save</button>}
        </div>
      </div>
    );
  }

  renderOwnerProfile() {
    console.log(this.ownerData);
    const owner = this.ownerData;
    if (!owner) return <p>Loading owner data...</p>;

    return (
      <div class="profile-container">
        <h2>Owner Profile</h2>
        <label>
          Full Name:
          <input type="text" value={owner.fullname} disabled={!this.editMode} onInput={e => this.handleInputChange(e, 'fullname', 'owner')} />
        </label>

        <label>
          Contact Number:
          <input type="text" value={owner.contact_number} disabled={!this.editMode} onInput={e => this.handleInputChange(e, 'contact_number', 'owner')} />
        </label>

        <label>
          Address:
          <input type="text" value={owner.address} disabled={!this.editMode} onInput={e => this.handleInputChange(e, 'address', 'owner')} />
        </label>
        {/* <label >
            Total Properties :<p>{owner.}</p>
        </label> */}
        <p>
          <b>Total Revenue:</b> ₹{owner.total_revenue}
        </p>

        <h3>Tenants</h3>
        {owner.tenants && owner.tenants.length > 0 ? (
          <ul>
            {owner.tenants.map(t => (
              <li>
                {t.username} ({t.contact_number})
              </li>
            ))}
          </ul>
        ) : (
          <p>No tenants found.</p>
        )}

        <div class="button-container">
          <button onClick={() => this.toggleEdit()}>{this.editMode ? 'Cancel' : 'Edit'}</button>
          {this.editMode && <button onClick={() => this.saveChanges()}>Save</button>}
        </div>
      </div>
    );
  }

  render() {
    return (
      <div>
        {this.role === 'tenant' && this.renderTenantProfile()}
        {this.role === 'owner' && this.renderOwnerProfile()}
      </div>
    );
  }
}
