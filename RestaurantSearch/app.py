from flask import Flask, render_template, request, redirect, session, url_for, flash
import pymysql
import pymongo

# flask app instance
app = Flask(__name__)

# Set a secret key for session management
app.secret_key = 'key'

# mysql database
db = pymysql.connect(host='localhost',
                     port=3306,
                     user='root',
                     password='',
                     database='company',
                     cursorclass=pymysql.cursors.DictCursor)
cursor = db.cursor()

# monogo database
client = pymongo.MongoClient('localhost', 27017)
mdb = client.DBIILabs
restaurants_collection = mdb['Restaurants']


# register page
@app.route('/register', methods=['GET', 'POST'])  # Route for the registration page
def register():
    if request.method == 'POST':  # Check if the request method is POST (form submission)
        username = request.form['username']  # Get the username from the form
        password = request.form['password']  # Get the password from the form
        
        # Check if the username already exists in the database
        cursor.execute('SELECT * FROM users WHERE username = %s', (username,))
        existing_user = cursor.fetchone()  # Fetch the first matching user from the query result
        
        if existing_user:  # If the username already exists
            flash('Username is taken!', 'error')  # Flash an error message indicating the username is taken
            return redirect(url_for('register'))  # Redirect back to the registration page
        else:  # If the username is not already taken
            # Insert the new user's username and password into the database
            cursor.execute('INSERT INTO users(username, password) VALUES (%s, %s)', (username, password))
            db.commit()  # Commit the transaction to save changes to the database
            return redirect(url_for('login'))  # Redirect to the login page after successful registration
    
    # If the request method is not POST (i.e., GET request) or the registration form is not submitted, render the registration page
    return render_template('register.html')


# Login Page
@app.route('/', methods=['GET', 'POST'])  # Route for the homepage, also handles login
@app.route('/login', methods=['GET', 'POST'])  # Route for the login page
def login():
    error = 'Incorrect username/password!'  # Default error message
    if request.method == 'POST':  # Check if the request method is POST (form submission)
        username = request.form['username']  # Get the username from the form
        password = request.form['password']  # Get the password from the form

        # Execute a SQL query to check if the provided username and password match any user in the database
        cursor.execute('SELECT * FROM users WHERE username = %s AND password = %s', (username, password))
        user = cursor.fetchone()  # Fetch the first matching user from the query result

        if user:  # If user exists (credentials are correct)
            session['username'] = user['username']  # Store the username in the session
            return redirect(url_for('search'))  # Redirect to the search page
        else:  # If user doesn't exist (credentials are incorrect)
            flash('Incorrect username/password!')  # Flash a message indicating incorrect credentials

    # If the request method is not POST or credentials are incorrect, render the login page with an error message
    return render_template('login.html', error=error)


@app.route('/search')
def search():
    if 'loggedin' in session:  # Check if user is logged in
        search_query = request.args.get('search_query')  # Get the query entered by the user from the request arguments
        search_type = request.args.get('search_type')  # Get the type selected from the drop-down menu

        restaurants = []  # Create an empty list to store restaurants

        # Perform search query based on search criteria if both query and type are provided
        if search_query and search_type:
            # Create a MongoDB query based on user input to perform a case-insensitive search
            query = {search_type: {'$regex': search_query, '$options': 'i'}}
            # Find restaurants matching the query in the MongoDB collection
            restaurants = restaurants_collection.find(query)

        # render search page with the variable restaurants to display the search results
        return render_template('search.html', restaurants=restaurants)
    


# logout function
@app.route('/logout/')
def logout():
    session.pop('username', None)  # remove username from session
    return redirect('/')  # redirect to login after logout

if __name__ == '__main__':
    app.run(debug=True)  
