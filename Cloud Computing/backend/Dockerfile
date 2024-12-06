# Gunakan image Node.js sebagai base image
FROM node:18

# Set working directory di dalam container
WORKDIR /app

# Copy file package.json dan package-lock.json untuk install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy seluruh kode aplikasi ke dalam container
COPY . .

# Set environment variable untuk production (optional, jika dibutuhkan)
# ENV NODE_ENV=production

# Expose port yang digunakan oleh aplikasi
# EXPOSE 5000

# Expose port yang digunakan oleh Cloud Run
ENV PORT=8080
EXPOSE 8080

# Jalankan aplikasi
CMD ["node", "index.js"]
