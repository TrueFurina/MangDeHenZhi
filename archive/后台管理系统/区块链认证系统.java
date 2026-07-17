/*
 * @Author: 张敏杰 2468001320@qq.com
 * @Date: 2025-08-21 23:33:18
 * @LastEditors: 张敏杰 2468001320@qq.com
 * @LastEditTime: 2025-08-22 00:51:19
 * @FilePath: \Code\Mangdehenzhi\后台管理系统\区块链认证系统.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
// 区块链认证服务
@Service
public class BlockchainCertificationService {
    
    @Autowired
    private HyperledgerFabricClient fabricClient;
    
    /**
     * 颁发技能认证证书
     */
    public CertificationResult issueCertification(
            String userId, 
            SkillAssessmentResult assessmentResult) {
        
        // 构建证书数据
        CertificationData certData = buildCertificationData(
            userId, assessmentResult);
        
        // 调用智能合约上链
        String transactionId = fabricClient.invokeSmartContract(
            "certificationContract", 
            "issueCertification",
            new String[]{userId, JSON.toJSONString(certData)}
        );
        
        return new CertificationResult(
            transactionId,
            certData.getCertHash(),
            "CERT_ISSUED"
        );
    }
    
    /**
     * 验证证书真实性
     */
    public VerificationResult verifyCertification(String certHash) {
        return fabricClient.querySmartContract(
            "certificationContract",
            "verifyCertification",
            new String[]{certHash}
        );
    }
}
// 区块链认证服务
@Service
public class BlockchainCertificationService {
    
    @Autowired
    private HyperledgerFabricClient fabricClient;
    
    /**
     * 颁发技能认证证书
     */
    public CertificationResult issueCertification(
            String userId, 
            SkillAssessmentResult assessmentResult) {
        
        // 构建证书数据
        CertificationData certData = buildCertificationData(
            userId, assessmentResult);
        
        // 调用智能合约上链
        String transactionId = fabricClient.invokeSmartContract(
            "certificationContract", 
            "issueCertification",
            new String[]{userId, JSON.toJSONString(certData)}
        );
        
        return new CertificationResult(
            transactionId,
            certData.getCertHash(),
            "CERT_ISSUED"
        );
    }
    
    /**
     * 验证证书真实性
     */
    public VerificationResult verifyCertification(String certHash) {
        return fabricClient.querySmartContract(
            "certificationContract",
            "verifyCertification",
            new String[]{certHash}
        );
    }
}
// 区块链认证服务
@Service
public class BlockchainCertificationService {
    
    @Autowired
    private HyperledgerFabricClient fabricClient;
    
    /**
     * 颁发技能认证证书
     */
    public CertificationResult issueCertification(
            String userId, 
            SkillAssessmentResult assessmentResult) {
        
        // 构建证书数据
        CertificationData certData = buildCertificationData(
            userId, assessmentResult);
        
        // 调用智能合约上链
        String transactionId = fabricClient.invokeSmartContract(
            "certificationContract", 
            "issueCertification",
            new String[]{userId, JSON.toJSONString(certData)}
        );
        
        return new CertificationResult(
            transactionId,
            certData.getCertHash(),
            "CERT_ISSUED"
        );
    }
    
    /**
     * 验证证书真实性
     */
    public VerificationResult verifyCertification(String certHash) {
        return fabricClient.querySmartContract(
            "certificationContract",
            "verifyCertification",
            new String[]{certHash}
        );
    }
}
// 区块链认证服务
@Service
public class BlockchainCertificationService {
    
    @Autowired
    private HyperledgerFabricClient fabricClient;
    
    /**
     * 颁发技能认证证书
     */
    public CertificationResult issueCertification(
            String userId, 
            SkillAssessmentResult assessmentResult) {
        
        // 构建证书数据
        CertificationData certData = buildCertificationData(
            userId, assessmentResult);
        
        // 调用智能合约上链
        String transactionId = fabricClient.invokeSmartContract(
            "certificationContract", 
            "issueCertification",
            new String[]{userId, JSON.toJSONString(certData)}
        );
        
        return new CertificationResult(
            transactionId,
            certData.getCertHash(),
            "CERT_ISSUED"
        );
    }
    
    /**
     * 验证证书真实性
     */
    public VerificationResult verifyCertification(String certHash) {
        return fabricClient.querySmartContract(
            "certificationContract",
            "verifyCertification",
            new String[]{certHash}
        );
    }
}
