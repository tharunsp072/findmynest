import { Component, h, Prop, State, Event, EventEmitter, Watch,  } from '@stencil/core';

@Component({
  tag: 'app-navbar',
  styleUrl: 'app-navbar.css',
  shadow: true,
})
export class AppNavBar {
  @State() activeMenu: string = 'home';
  @Prop({ mutable: true, reflect: true }) role: string;

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
            <li class={this.activeMenu === 'home' ? 'active' : ''} onClick={() => this.handleNavClick('home')}>
              Home
            </li>
            {/* 
            <li class={this.activeMenu === 'properties' ? 'active' : ''} onClick={() => this.handleNavClick('properties')}>
              Properties
            </li> */}

            {this.role !== 'TENANT' && (
              <li class={this.activeMenu === 'my-properties' ? 'active' : ''} onClick={() => this.handleNavClick('my-properties')}>
                My Listings
              </li>
            )}

            {this.role !== 'TENANT' && (
              <li class={this.activeMenu === 'add-property' ? 'active' : ''} onClick={() => this.handleNavClick('add-property')}>
                Add Property
              </li>
            )}

            {this.role === 'OWNER' && <li onClick={() => this.handleNavClick('owner-payments')}>Payments Received</li>}
            {this.role === 'OWNER' && <li onClick={() => this.handleNavClick('owner-inquires')}>Inquiries</li>}

            {this.role === 'TENANT' && <li onClick={() => this.handleNavClick('payments')}>Payment History</li>}
            {this.role === 'TENANT' && <li onClick={() => this.handleNavClick('inquiries')}>Inquiry</li>}

            <li class={this.activeMenu === 'profile' ? 'active' : ''} onClick={() => this.handleNavClick('profile')}>
              Profile
            </li>
            <li class={this.activeMenu === 'favorites' ? 'active' : ''} onClick={() => this.handleNavClick('favorites')}>
              ❤️
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