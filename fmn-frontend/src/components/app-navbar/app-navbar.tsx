import { Component, h, Prop, State, Event, EventEmitter, Watch } from '@stencil/core';

@Component({
  tag: 'app-navbar',
  styleUrl: 'app-navbar.css',
  shadow: true,
})
export class AppNavBar {
  @Prop() activeMenu: string = 'home';
  @Prop() role: string;
  @Event() handleNav: EventEmitter<string>;

  handleNavClick(activeMenu: string) {
    this.handleNav.emit(activeMenu);
  }
  componentWillLoad() {
    console.log(this.role);
  }

  @Watch('role')
  roleChanged(newValue: string) {
    console.log('Role changed:', newValue);
  }

  handleLogout() {
    localStorage.clear();
    window.location.href = '/';
  }

  render() {
    console.log('Navbar rendering with role:', this.role);
    if (!this.role) return null;
    return (
      <div class="layout">
        <nav class="main-navbar">
          <h2>FindMyNest</h2>
          <div class={`${this.role}-search-bar`}>
            <input type="text" id="search-inp" placeholder="Search properties..." />
          </div>
          <ul class="nav-links">
            <li class={this.activeMenu === 'home' ? 'active' : ''} onClick={() => (this.activeMenu = 'home')}>
              <stencil-route-link url="/dashboard/home">Home</stencil-route-link>
            </li>

            {this.role !== 'TENANT' && (
              <li class={this.activeMenu === 'my-properties' ? 'active' : ''} onClick={() => (this.activeMenu = 'my-properties')}>
                <stencil-route-link url="/dashboard/my-properties">My Listings</stencil-route-link>
              </li>
            )}

            {this.role !== 'TENANT' && (
              <li class={this.activeMenu === 'add-property' ? 'active' : ''} onClick={() => (this.activeMenu = 'add-property')}>
                <stencil-route-link url="/dashboard/add-property">Add Property</stencil-route-link>
              </li>
            )}

            {this.role === 'OWNER' && (
              <li class={this.activeMenu === 'payments' ? 'active' : ''} onClick={() => (this.activeMenu = 'payments')}>
                <stencil-route-link url="/dashboard/payments">Payments Received</stencil-route-link>
              </li>
            )}
            {this.role === 'OWNER' && (
              <li class={this.activeMenu === 'inquiry' ? 'active' : ''} onClick={() => (this.activeMenu = 'inquiry')}>
                {' '}
                <stencil-route-link url="/dashboard/inquiries">Inquiries</stencil-route-link>
              </li>
            )}
            {this.role === 'TENANT' && (
              <li class={this.activeMenu === 'myRentals' ? 'active' : ''} onClick={() => (this.activeMenu = 'myRentals')}>
                {' '}
                <stencil-route-link url="/dashboard/myRentals">My Rentals</stencil-route-link>
              </li>
            )}

            {this.role === 'TENANT' && (
              <li class={this.activeMenu === 'payments' ? 'active' : ''} onClick={() => (this.activeMenu = 'payments')}>
                {' '}
                <stencil-route-link url="/dashboard/payments">Payments History</stencil-route-link>
              </li>
            )}
            {this.role === 'TENANT' && (
              <li class={this.activeMenu === 'inquiry' ? 'active' : ''} onClick={() => (this.activeMenu = 'inquiry')}>
                {' '}
                <stencil-route-link url="/dashboard/inquiries">Inquiry</stencil-route-link>
              </li>
            )}

            <li class={this.activeMenu === 'profile' ? 'active' : ''} onClick={() => (this.activeMenu = 'profile')}>
              <stencil-route-link url="/dashboard/profile">Profile</stencil-route-link>
            </li>
            <li class={this.activeMenu === 'favorites' ? 'active' : ''} onClick={() => (this.activeMenu = 'profile')}>
              <stencil-route-link url="/dashboard/favorites"> ❤️</stencil-route-link>
            </li>
          </ul>

          <button class="logout-btn" onClick={() => this.handleLogout()}>
            Logout
          </button>
        </nav>
      </div>
    );
  }
}
