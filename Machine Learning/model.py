import pandas as pd
import numpy as np
from keras.models import load_model
import joblib
from flask import Flask, request, jsonify, render_template
import logging

# Memuat model dan scaler
try:
    classification_model = load_model('Model/classification_model.h5')
    classification_scaler = joblib.load('Model/scaler_classification.pkl')
    regression_model = load_model('Model/deep_learning_regression_model.h5')
    regression_scaler = joblib.load('Model/scaler_deep_learning_regression.pkl')
    print("Model dan scaler berhasil di-load.")
except Exception as e:
    print(f"Error saat me-load model atau scaler: {e}")
    raise  # Raise exception untuk menghentikan aplikasi

# Fungsi predict_rad()
def predict_rad(features, scaler, model):
    """
    Prediksi nilai rad(m) berdasarkan fitur cuaca input.

    Parameters:
        features (dict): Dictionary berisi nilai semua fitur.
        scaler (StandardScaler): Scaler yang digunakan untuk normalisasi fitur.
        model (Sequential): Model deep learning yang sudah dilatih.

    Returns:
        float: Nilai prediksi rad(m).
    """
    try:
        # Konversi input features ke DataFrame dengan nama kolom yang sama dengan yang diharapkan scaler
        feature_df = pd.DataFrame([features])

        # Penskalaan fitur input menggunakan scaler yang sudah dilatih
        scaled_features = scaler.transform(feature_df)

        # Prediksi rad(m) menggunakan model yang sudah dilatih
        predicted_value = model.predict(scaled_features)

        # Kembalikan nilai prediksi rad(m)
        return predicted_value[0][0]
    except Exception as e:
        print(f"Error dalam prediksi rad(m): {e}")
        raise

# Fungsi predict_condition()
def predict_condition(input_features, scaler, model):
    try:
        # Data input baru
        new_data = np.array([[input_features['Tn'], input_features['Tx'], input_features['Tavg'], 
                              input_features['RH_avg'], input_features['ff_avg'], input_features['rad_m']]])

        # Normalisasi data baru menggunakan scaler yang sama seperti yang digunakan saat pelatihan
        new_data_scaled = scaler.transform(new_data)

        # Reshape data untuk input LSTM (3D tensor)
        new_data_reshaped = new_data_scaled.reshape((new_data_scaled.shape[0], 1, new_data_scaled.shape[1]))

        # Gunakan model yang dimuat untuk prediksi
        prediction = model.predict(new_data_reshaped)

        # Tampilkan hasil prediksi (nilai probabilitas, 0 atau 1)
        predicted_label = (prediction > 0.5).astype(int)
        return 'Aman' if predicted_label[0][0] == 0 else 'Tidak Aman'
    except Exception as e:
        print(f"Error dalam prediksi kondisi: {e}")
        raise