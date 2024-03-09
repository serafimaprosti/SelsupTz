import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private long countOfRequests;
    private TimeUnit timeUnit;
    private long limitOfRequests;
    private long duration;
    private long firstTimeMethodUse;
    private long flagFirstTimeMethodUse;

    public CrptApi(TimeUnit timeUnit, long duration, long limitOfRequests){
        this.timeUnit = timeUnit;
        this.duration = duration;
        this.limitOfRequests = limitOfRequests;
        this.countOfRequests = 0;
        this.flagFirstTimeMethodUse = 0;
    }



    public void doRequest(final Document document, final String sign){
        if (this.flagFirstTimeMethodUse == 0){
            flagFirstTimeMethodUse++;
            this.firstTimeMethodUse = System.currentTimeMillis();
        }

        if (this.countOfRequests == this.limitOfRequests){
            long currentTime = System.currentTimeMillis();
            long allowedTimeToDoRequests = this.firstTimeMethodUse + this.timeUnit.toMillis(this.duration);
            if (currentTime < allowedTimeToDoRequests){
                try {
                    Thread.sleep(allowedTimeToDoRequests - currentTime);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
            this.countOfRequests = 0;
            this.firstTimeMethodUse = System.currentTimeMillis();
        }

        this.countOfRequests++;
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonDocument;
        try {
            jsonDocument = objectMapper.writeValueAsString(document);
        }catch (JsonProcessingException exception) {
            throw new RuntimeException("Error parsing Document", exception);
        }
        StringEntity stringEntity = new StringEntity(jsonDocument);

        HttpPost httpPost = new HttpPost("https://ismp.crpt.ru/api/v3/lk/documents/create");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(stringEntity);

        try (
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute(httpPost);
        ) {
        }catch (IOException exception){
            throw new RuntimeException("Error executing HTTP request", exception);
        }
    }






    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLimitOfRequests() {
        return limitOfRequests;
    }
    public void setRequestLimit(long limitOfRequests) {
        this.limitOfRequests = limitOfRequests;
    }

    public class Document {
        private Description description;
        private String docId;
        private String docStatus;
        private String docType;
        private boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private String producerInn;
        private String productionDate;
        private String productionType;
        private List<Product> products;
        private String regDate;
        private String regNumber;

        @JsonProperty("description")
        public Description getDescription() {
            return description;
        }
        public void setDescription(Description description) {
            this.description = description;
        }

        @JsonProperty("doc_id")
        public String getDocId() {
            return docId;
        }
        public void setDocId(String docId) {
            this.docId = docId;
        }

        @JsonProperty("doc_status")
        public String getDocStatus() {
            return docStatus;
        }
        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }

        @JsonProperty("doc_type")
        public String getDocType() {
            return docType;
        }
        public void setDocType(String docType) {
            this.docType = docType;
        }

        @JsonProperty("importRequest")
        public boolean isImportRequest() {
            return importRequest;
        }
        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        @JsonProperty("owner_inn")
        public String getOwnerInn() {
            return ownerInn;
        }
        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        @JsonProperty("participant_inn")
        public String getParticipantInn() {
            return participantInn;
        }
        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        @JsonProperty("producer_inn")
        public String getProducerInn() {
            return producerInn;
        }
        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        @JsonProperty("production_date")
        public String getProductionDate() {
            return productionDate;
        }
        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        @JsonProperty("production_type")
        public String getProductionType() {
            return productionType;
        }
        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }

        @JsonProperty("products")
        public List<Product> getProducts() {
            return products;
        }
        public void setProducts(List<Product> products) {
            this.products = products;
        }

        @JsonProperty("reg_date")
        public String getRegDate() {
            return regDate;
        }
        public void setRegDate(String regDate) {
            this.regDate = regDate;
        }

        @JsonProperty("reg_number")
        public String getRegNumber() {
            return regNumber;
        }
        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Document document = (Document) object;
            return importRequest == document.importRequest && Objects.equals(description, document.description) && Objects.equals(docId, document.docId) && Objects.equals(docStatus, document.docStatus) && Objects.equals(docType, document.docType) && Objects.equals(ownerInn, document.ownerInn) && Objects.equals(participantInn, document.participantInn) && Objects.equals(producerInn, document.producerInn) && Objects.equals(productionDate, document.productionDate) && Objects.equals(productionType, document.productionType) && Objects.equals(products, document.products) && Objects.equals(regDate, document.regDate) && Objects.equals(regNumber, document.regNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(description, docId, docStatus, docType, importRequest, ownerInn, participantInn, producerInn, productionDate, productionType, products, regDate, regNumber);
        }
    }
    public class Description {
        private String participantInn;

        @JsonProperty("participantInn")
        public String getParticipantInn() {
            return participantInn;
        }
        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Description that = (Description) object;
            return Objects.equals(participantInn, that.participantInn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(participantInn);
        }
    }

    public class Product {
        private String certificateDocument;
        private String certificateDocumentDate;
        private String certificateDocumentNumber;
        private String ownerInn;
        private String producerInn;
        private String productionDate;
        private String tnvedCode;
        private String uitCode;
        private String uituCode;

        @JsonProperty("certificate_document")
        public String getCertificateDocument() {
            return certificateDocument;
        }
        public void setCertificateDocument(String certificateDocument) {
            this.certificateDocument = certificateDocument;
        }

        @JsonProperty("certificate_document_date")
        public String getCertificateDocumentDate() {
            return certificateDocumentDate;
        }
        public void setCertificateDocumentDate(String certificateDocumentDate) {
            this.certificateDocumentDate = certificateDocumentDate;
        }

        @JsonProperty("certificate_document_number")
        public String getCertificateDocumentNumber() {
            return certificateDocumentNumber;
        }
        public void setCertificateDocumentNumber(String certificateDocumentNumber) {
            this.certificateDocumentNumber = certificateDocumentNumber;
        }

        @JsonProperty("owner_inn")
        public String getOwnerInn() {
            return ownerInn;
        }
        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        @JsonProperty("producer_inn")
        public String getProducerInn() {
            return producerInn;
        }
        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        @JsonProperty("production_date")
        public String getProductionDate() {
            return productionDate;
        }
        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        @JsonProperty("tnved_code")
        public String getTnvedCode() {
            return tnvedCode;
        }
        public void setTnvedCode(String tnvedCode) {
            this.tnvedCode = tnvedCode;
        }

        @JsonProperty("uit_code")
        public String getUitCode() {
            return uitCode;
        }
        public void setUitCode(String uitCode) {
            this.uitCode = uitCode;
        }

        @JsonProperty("uitu_code")
        public String getUituCode() {
            return uituCode;
        }
        public void setUituCode(String uituCode) {
            this.uituCode = uituCode;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Product product = (Product) object;
            return Objects.equals(certificateDocument, product.certificateDocument) && Objects.equals(certificateDocumentDate, product.certificateDocumentDate) && Objects.equals(certificateDocumentNumber, product.certificateDocumentNumber) && Objects.equals(ownerInn, product.ownerInn) && Objects.equals(producerInn, product.producerInn) && Objects.equals(productionDate, product.productionDate) && Objects.equals(tnvedCode, product.tnvedCode) && Objects.equals(uitCode, product.uitCode) && Objects.equals(uituCode, product.uituCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(certificateDocument, certificateDocumentDate, certificateDocumentNumber, ownerInn, producerInn, productionDate, tnvedCode, uitCode, uituCode);
        }
    }
}
