#!/bin/bash

echo "🚀 Setting up Online Course Management System (OCMS)"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java version $JAVA_VERSION is too old. Please install Java 17 or higher."
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

echo "✅ Maven version: $(mvn -version | head -n 1)"

# Check if MySQL is running
if ! command -v mysql &> /dev/null; then
    echo "⚠️  MySQL client is not installed. Please ensure MySQL server is running."
else
    echo "✅ MySQL client found"
fi

echo ""
echo "📦 Building the project..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "🎯 To run the application:"
    echo "   mvn spring-boot:run"
    echo ""
    echo "🌐 The application will be available at: http://localhost:8080"
    echo ""
    echo "📚 API Documentation will be available at: http://localhost:8080/swagger-ui.html"
    echo ""
    echo "🔧 Make sure to update the database configuration in src/main/resources/application.yml"
    echo "   if your MySQL credentials are different from the defaults."
else
    echo "❌ Build failed!"
    exit 1
fi