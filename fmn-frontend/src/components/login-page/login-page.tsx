import { Component, h, State } from '@stencil/core';

@Component({
  tag: 'login-page',
  styleUrl: 'login-page.css',
  shadow: true,
})
export class LoginPage {
  @State() loginUser: boolean = true;
  @State() registerUserlink: boolean = true;
  @State() registerUser: boolean = false;
  @State() registerError : string ='';

  @State() email: string = '';
  @State() password: string = '';
  @State() error: string = '';

  @State() selectedRole: string = ''; 
  @State() registerNewUser = {
    username: '',
    email: '',
    password: '',
  };


  handleRegister() {
    this.loginUser = !this.loginUser;
    this.registerUserlink = !this.registerUserlink;
    this.registerUser = !this.registerUser;
  }


  handleRoleSelection(role: string) {
    this.selectedRole = role;
    console.log('Selected Role:', role);
  }

  handleRegisterInputChange(e: Event, field: string) {
    const input = e.target as HTMLInputElement;
    this.registerNewUser = {
      ...this.registerNewUser,
      [field]: input.value,
    };
  }

  async handleUserRegistration(e: Event) {
    e.preventDefault();

    if (!this.selectedRole) {
      this.registerError = "Please select a role";
      return;
    }

    const registerPayload = {
      username: this.registerNewUser.username,
      email: this.registerNewUser.email,
      password: this.registerNewUser.password,
      role: this.selectedRole,
    };

    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerPayload),
      });

       const data = await response.json().catch(() => null);

       if (!response.ok) {
         const errorMsg = data?.message || 'Registration failed. Please try again.';
         if (errorMsg.includes('Email already in use')) {
           this.registerError = 'Email already in use. Login instead.';
         } else {
           this.registerError = errorMsg;
         }
         return;
       }
      this.handleRegister(); 
    } catch (err) {
      console.error('Registration error:', err);
      alert('Registration failed. ' + err.message);
    }
  }

  async handlelogin(e: Event) {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          emailorUsername: this.email,
          password: this.password,
        }),
      });

      if (!response.ok) throw new Error('Invalid Credentials');

      const data = await response.json();
      localStorage.setItem('token', data.token);
      localStorage.setItem('userId', data.userId);
      localStorage.setItem('profileId', data.profileId);
      localStorage.setItem('role', data.roles);
      window.location.href = '/dashboard';
    } catch (err) {
      console.error(err);
      this.error = 'Invalid email or password';
    }
  }

  handleEmailChange(e: Event) {
    const input = e.target as HTMLInputElement;
    this.email = input.value;
  }

  handlePasswordInput(e: Event) {
    const passInput = e.target as HTMLInputElement;
    this.password = passInput.value;
  }

  render() {
    return (
      <div class="layout">
        <nav class="login-navbar">
          <h2>FindMyNest</h2>
        </nav>

        <main class="main-content">
          <div class="description-and-carasoul">
            <div class="carasoul">
              <div class="slides">
                <img src="/assets/new.jpg" alt="no image" />
                <img src="/assets/new1.jpg" alt="no image" />
                <img src="/assets/new2.jpg" alt="no image" />
                <img src="/assets/new3.jpg" alt="no image" />
              </div>

              <div class="description">
                <p>
                  FindMyNest is a smart property rental platform where owners can list their properties and tenants can discover, inquire, and rent homes easily — all in one place.
                </p>
                <p>
                  It offers secure logins, detailed property listings with images, search and filters by location, price, and type, and personalized dashboards for owners and
                  tenants.
                </p>
                <p>Whether you’re looking to rent your property or find your dream home, FindMyNest helps you do it faster, safer, and smarter.</p>
              </div>
            </div>
          </div>

          <div class="login-or-register">
            {this.loginUser && (
              <form onSubmit={e => this.handlelogin(e)} class="login-form">
                <h3>Login</h3>
                <div class="inp-div">
                  <div class="email-inp-div">
                    <input type="text" placeholder="email / username" value={this.email} onInput={e => this.handleEmailChange(e)} required />
                  </div>
                  <div class="pass-inp-div">
                    <input type="password" placeholder="password" value={this.password} onInput={e => this.handlePasswordInput(e)} required />
                  </div>
                  <button type="submit" id="continue-btn">
                    Continue
                  </button>
                </div>
                {this.error && <p>{this.error}</p>}
              </form>
            )}

            {this.registerUserlink && (
              <p>
                Don’t have an account? <button onClick={() => this.handleRegister()}>Register Now</button>
              </p>
            )}

            {this.registerUser && (
              <div class="registration-form">
                <div class="registration-header">
                  <button onClick={() => this.handleRegister()}>
                    <img src="/assets/arrow.png" alt="back" />
                  </button>
                  <h4>Sign Up</h4>
                </div>

                <div class="registration-option-btns">
                  <button type="button" class={this.selectedRole === 'TENANT' ? 'active' : ''} onClick={() => this.handleRoleSelection('TENANT')}>
                    Tenant
                  </button>
                  <button type="button" class={this.selectedRole === 'OWNER' ? 'active' : ''} onClick={() => this.handleRoleSelection('OWNER')}>
                    Owner
                  </button>
                </div>

                <form onSubmit={e => this.handleUserRegistration(e)} class="register-form">
                  <input type="text" placeholder="Username" value={this.registerNewUser.username} onInput={e => this.handleRegisterInputChange(e, 'username')} required />
                  <input type="email" placeholder="Email" value={this.registerNewUser.email} onInput={e => this.handleRegisterInputChange(e, 'email')} required />
                  <input type="password" placeholder="Password" value={this.registerNewUser.password} onInput={e => this.handleRegisterInputChange(e, 'password')} required />
                  <button type="submit">Register</button>

                  {this.registerError && <p style={{ color: 'red', fontSize: '0.9em', marginTop: '8px' }}>{this.registerError}</p>}
                </form>

                <p>
                  Already registered?{' '}
                  <button type="button" onClick={() => this.handleRegister()}>
                    Login Now
                  </button>
                </p>
              </div>
            )}
          </div>
        </main>
      </div>
    );
  }
}
